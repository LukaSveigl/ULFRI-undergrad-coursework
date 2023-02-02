using sfl.Data;
using sfl.Models;
using System;
using System.Linq;
using System.Threading.Tasks;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
namespace sfl.Controllers
{
    [Authorize(Roles = "Administrator, Warehouse manager, Warehouse worker, Logistics agent, Delivery driver")]
    public class JobsController : Controller
    {
        private readonly CompanyContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly string[] _navigationProperties;

        public JobsController(CompanyContext context, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
            _navigationProperties = new string[] { "JobsParcels", "Staff", "JobType", "JobStatus", "JobsParcels" };
        }

        // GET: Jobs
        public async Task<IActionResult> Index()
        {
            var currentUser = await _userManager.GetUserAsync(User);

            var currentRole = currentUser == null ? null :
                _context.UserRoles.Select(ur => new { ur.RoleId, ur.UserId })
                    .Where(ur => ur.UserId == currentUser.Id)
                    .First().RoleId;
            var currentBranch = currentUser == null ? -1 :
                _context.Staff.Select(s => new { s.Username, s.BranchID })
                    .Where(s => s.Username == currentUser.UserName)
                    .ToList()[0].BranchID;

            if (currentRole == null || currentRole == null || currentBranch == -1)
            {
                return Problem("Entity set 'CompanyContext.Jobs'  is null.");
            }

            List<Job>? view = currentRole switch
            {
                "1" => await _context.Jobs.ToListAsync(),
                "2" => await _context.Jobs.Select(j => j)
                                        .Where(j => j.Staff.BranchID == currentBranch).ToListAsync(),
                "3" => await _context.Jobs.Select(j => j)
                                        .Where(j => j.StaffUsername == currentUser.UserName).ToListAsync(),
                "4" => await _context.Jobs.Select(j => j).ToListAsync(),
                "5" => await _context.Jobs.Select(j => j)
                                        .Where(j => j.StaffUsername == currentUser.UserName).ToListAsync(),
                _ => null,
            };

            if (view != null)
            {
                for (int i = 0; i < view.Count; i++)
                {
                    view[i].JobsParcels = _context.JobsParcels
                        .Select(jp => jp)
                        .Where(jp => jp.JobID == view[i].ID)
                        .ToList();

                    view[i].JobStatus = _context.JobStatuses
                        .Select(js => js)
                        .Where(js => js.ID == view[i].JobStatusID)
                        .First();

                    view[i].JobType = _context.JobTypes
                        .Select(jt => jt)
                        .Where(jt => jt.ID == view[i].JobTypeID)
                        .First();
                }

            }

            return view != null ? View(view) : Problem("Entity set 'CompanyContext.Jobs'  is null.");
        }

        // GET: Jobs/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.Jobs == null)
            {
                return NotFound();
            }

            var job = await _context.Jobs
                .FirstOrDefaultAsync(m => m.ID == id);

            job.JobsParcels = _context.JobsParcels
                .Select(jp => jp)
                .Where(jp => jp.JobID == job.ID)
                .ToList();

            if (job == null)
            {
                return NotFound();
            }

            return View(job);
        }

        // GET: Jobs/Create
        [Authorize(Roles = "Administrator, Warehouse manager, Logistics agent")]
        public IActionResult Create()
        {
            var currentUserName = _userManager.GetUserName(User);
            var currentUserId = _userManager.GetUserId(User);
            var roleId = _context.UserRoles
                .Select(ur => ur)
                .Where(ur => ur.UserId == currentUserId)
                .First().RoleId;

            ViewData["Parcels"] = new SelectList(_context.Parcels, "ID", "ID");

            if (roleId == "1")
            {
                ViewData["StaffUsername"] = new SelectList(_context.Staff, "Username", "Username");
            }
            else
            {
                var currentBranch = currentUserName == null ? -1 :
                    _context.Staff.Select(s => new { s.Username, s.BranchID })
                        .Where(s => s.Username == currentUserName)
                        .First().BranchID;

                ViewData["StaffUsername"] = new SelectList(_context.Staff
                    .Select(s => s)
                    .Where(s => s.BranchID == currentBranch),
                "Username", "Username");
            }

            ViewData["JobTypeID"] = new SelectList(_context.JobTypes.Where(jt => (jt.ID != 1) && (jt.ID != 2) && (jt.ID != 4)), "ID", "Name");
            return View();
        }

        // POST: Jobs/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = "Administrator, Warehouse manager, Logistics agent")]
        public async Task<IActionResult> Create([Bind("ID,StaffUsername,JobTypeID,ParcelIDs")] Job job)
        {
            var ids = Request.Form["ParcelIDs"].ToList();
            RemoveNavigationProperties();
            if (ModelState.IsValid)
            {
                job.DateCreated = DateTime.Now;
                job.JobStatusID = _context.JobStatuses.Select(j => j.ID).Where(j => j == 1).First();
                // First create job.
                _context.Add(job);
                await _context.SaveChangesAsync();

                // Create JobParcel records from selected parcels and current job.
                var parcels = new List<JobParcel>();
                foreach (var pID in Request.Form["ParcelIDs"].ToList())
                {
                    parcels.Add(new JobParcel { ParcelID = pID, JobID = job.ID });
                }

                // Link jobs and selected parcels in table JobParcel.
                _context.JobsParcels.AddRange(parcels);
                await _context.SaveChangesAsync();

                return RedirectToAction(nameof(Index));
            }

            var currentUserName = _userManager.GetUserName(User);
            var currentUserId = _userManager.GetUserId(User);
            var roleId = _context.UserRoles
                .Select(ur => ur)
                .Where(ur => ur.UserId == currentUserId)
                .First().RoleId;

            if (roleId == "1")
            {
                ViewData["StaffUsername"] = new SelectList(_context.Staff, "Username", "Username");
            }
            else
            {
                var currentBranch = currentUserName == null ? -1 :
                    _context.Staff.Select(s => new { s.Username, s.BranchID })
                        .Where(s => s.Username == currentUserName)
                        .First().BranchID;

                ViewData["StaffUsername"] = new SelectList(_context.Staff
                    .Select(s => s)
                    .Where(s => s.BranchID == currentBranch),
                "Username", "Username");
            }

            ViewData["Parcels"] = new SelectList(_context.Parcels, "ID", "ID");
            ViewData["JobTypeID"] = new SelectList(_context.JobTypes.Where(jt => (jt.ID != 1) && (jt.ID != 2) && (jt.ID != 4)), "ID", "Name");
            return View(job);
        }

        // GET: Jobs/Edit/5
        [Authorize(Roles = "Administrator, Warehouse manager, Warehouse worker, Delivery driver")]
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.Jobs == null)
            {
                return NotFound();
            }

            ViewData[index: "JobStatusID"] = new SelectList(_context.JobStatuses, "ID", "Name");

            var job = await _context.Jobs.FindAsync(id);
            if (job == null)
            {
                return NotFound();
            }
            return View(job);
        }

        // POST: Jobs/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = "Administrator, Warehouse manager, Warehouse worker, Delivery driver")]
        public async Task<IActionResult> Edit(int id, [Bind("ID,DateCreated,DateCompleted,StaffUsername,JobStatusID,JobTypeID")] Job job)
        {
            if (id != job.ID)
            {
                return NotFound();
            }

            if (job.DateCompleted < job.DateCreated)
            {
                ViewData[index: "JobStatusID"] = new SelectList(_context.JobStatuses, "ID", "Name");
                return View(job);
            }

            RemoveNavigationProperties();
            ModelState.Remove("StaffUsername");
            if (ModelState.IsValid)
            {
                try
                {
                    if (job.JobStatusID == 2)
                    {
                        job.DateCompleted = DateTime.Now;
                    }

                    job.StaffUsername = _context.Jobs.Select(j => new { j.StaffUsername, j.ID })
                        .Where(j => j.ID == job.ID).ToList()[0].StaffUsername;
                    _context.Update(job);
                    await _context.SaveChangesAsync();

                    MoveJob(job);

                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!JobExists(job.ID))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            ViewData[index: "JobStatusID"] = new SelectList(_context.JobStatuses, "ID", "Name");
            return View(job);
        }

        // GET: Jobs/Delete/5
        [Authorize(Roles = "Administrator, Warehouse manager")]
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.Jobs == null)
            {
                return NotFound();
            }
            var job = await _context.Jobs
                .FirstOrDefaultAsync(m => m.ID == id);
            if (job == null)
            {
                return NotFound();
            }

            return View(job);
        }

        // POST: Jobs/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        [Authorize(Roles = "Administrator, Warehouse manager")]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.Jobs == null)
            {
                return Problem("Entity set 'CompanyContext.Jobs'  is null.");
            }
            var job = await _context.Jobs.FindAsync(id);
            if (job != null)
            {
                _context.Jobs.Remove(job);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool JobExists(int id)
        {
            return (_context.Jobs?.Any(e => e.ID == id)).GetValueOrDefault();
        }

        private void RemoveNavigationProperties()
        {
            foreach (var key in _navigationProperties)
            {
                ModelState.Remove(key);
            }
        }

        public void MoveJob(Job job)
        {
            if (job == null || job.JobStatusID != 2)
            {
                return;
            }

            job.ParcelIDs = _context.Jobs.Select(j => new { j.ID, j.ParcelIDs }).Where(j => j.ID == job.ID).ToList()[0].ParcelIDs;

            job.ParcelIDs = _context.JobsParcels.Select(jp => new { jp.JobID, jp.ParcelID })
                .Where(jp => jp.JobID == job.ID).Select(jp => jp.ParcelID).ToList();

            Staff jobEmployee = _context.Staff.Select(s => s).Where(s => s.Username == job.StaffUsername).First();
            int jobEmployeeBranchID = jobEmployee.BranchID;

            Dictionary<String, List<String>> locationsToParcelIDs = new()
            {
                { "Skladišče LJ", new List<String>() },
                { "Skladišče MB", new List<String>() },
                { "Skladišče KP", new List<String>() },
                { "Skladišče NM", new List<String>() }
            };

            Dictionary<String, int> branchNameToID = new()
            {
                { "Skladišče LJ", 1 },
                { "Skladišče MB", 2 },
                { "Skladišče KP", 3 },
                { "Skladišče NM", 4 }
            };

            if (jobEmployee.RoleID == 3)
            {
                if (job.JobTypeID == 3)
                {
                    // Sort the parcels into buckets by their destination.
                    foreach (String parcelID in job.ParcelIDs)
                    {
                        Parcel currentParcel = _context.Parcels.Select(p => p).Where(p => p.ID == parcelID).First();
                        int recipientCityCode = int.Parse(currentParcel.RecipientCode);

                        if ((recipientCityCode >= 1000 && recipientCityCode < 2000) || (recipientCityCode >= 4000 && recipientCityCode < 5000))
                        {
                            locationsToParcelIDs["Skladišče LJ"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 2000 && recipientCityCode < 3000) || (recipientCityCode >= 5000 && recipientCityCode < 6000))
                        {
                            locationsToParcelIDs["Skladišče MB"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 3000 && recipientCityCode < 4000) || (recipientCityCode >= 6000 && recipientCityCode < 7000))
                        {
                            locationsToParcelIDs["Skladišče KP"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 7000 && recipientCityCode < 8000) || (recipientCityCode >= 9000 && recipientCityCode < 10000))
                        {
                            locationsToParcelIDs["Skladišče NM"].Add(parcelID);
                        }
                    }

                    int currentBranchID = jobEmployee.BranchID;

                    foreach (String key in locationsToParcelIDs.Keys)
                    {
                        foreach (String parcelID in locationsToParcelIDs[key])
                        {
                            int nextBranchID = 0;
                            int nextJobTypeID = 0;

                            // If current branch is final branch.
                            if (currentBranchID == branchNameToID[key])
                            {
                                nextBranchID = currentBranchID;
                                nextJobTypeID = 7;
                            }
                            // If current branch is not final branch.
                            else
                            {
                                nextJobTypeID = 5;
                                if (currentBranchID != 1)
                                {
                                    nextBranchID = 1;
                                }
                                else
                                {
                                    nextBranchID = branchNameToID[key];
                                }
                            }

                            List<Staff> staff = _context.Staff.Select(s => s)
                                    .Where(s => (s.BranchID == nextBranchID) && (s.RoleID == 5))
                                    .ToList();

                            Staff driver = staff[new Random().Next(staff.Count)];

                            _context.Add(new Job()
                            {
                                JobTypeID = nextJobTypeID,
                                JobStatusID = 1,
                                StaffUsername = driver.Username,
                                DateCreated = DateTime.Now,
                                ParcelIDs = new List<String>() { parcelID }
                            });
                            _context.SaveChanges();

                            // Link last created job to parcel.
                            _context.JobsParcels.Add(new JobParcel
                            {
                                JobID = _context.Jobs.Select(j => j)
                                            .Where(j => j.JobTypeID == nextJobTypeID && j.JobStatusID == 1 && j.StaffUsername == driver.Username)
                                            .Select(j => j.ID)
                                            .Max(), // Get last job.
                                ParcelID = parcelID
                            });
                            _context.SaveChanges();

                            if (nextJobTypeID == 7)
                            {
                                Parcel p = _context.Parcels.Select(p => p).Where(p => p.ID == parcelID).First();
                                p.ParcelStatusID = 2;
                                _context.Update(p);
                                _context.SaveChanges();
                            }
                        }
                    }
                }
            }
            else if (jobEmployee.RoleID == 5)
            {
                // If cargo departing confirmation, create job for themselves.
                if (job.JobTypeID == 5)
                {
                    _context.Add(new Job()
                    {
                        JobTypeID = 6,
                        JobStatusID = 1,
                        StaffUsername = jobEmployee.Username,
                        DateCreated = DateTime.Now,
                        ParcelIDs = job.ParcelIDs
                    });
                    _context.SaveChanges();

                    foreach (String parcelID in job.ParcelIDs)
                    {
                        // Link last created job to parcel.
                        _context.JobsParcels.Add(new JobParcel
                        {
                            JobID = _context.Jobs.Select(j => j)
                                        .Where(j => j.JobTypeID == 6 && j.JobStatusID == 1 && j.StaffUsername == jobEmployee.Username)
                                        .Select(j => j.ID)
                                        .Max(), // Get last job.
                            ParcelID = parcelID
                        });
                        _context.SaveChanges();
                    }
                }
                // If cargo arrival confirmation, create job for next warehouse.
                else if (job.JobTypeID == 6)
                {
                    foreach (String parcelID in job.ParcelIDs)
                    {
                        Parcel currentParcel = _context.Parcels.Select(p => p).Where(p => p.ID == parcelID).First();
                        int recipientCityCode = int.Parse(currentParcel.RecipientCode);

                        if ((recipientCityCode >= 1000 && recipientCityCode < 2000) || (recipientCityCode >= 4000 && recipientCityCode < 5000))
                        {
                            locationsToParcelIDs["Skladišče LJ"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 2000 && recipientCityCode < 3000) || (recipientCityCode >= 5000 && recipientCityCode < 6000))
                        {
                            locationsToParcelIDs["Skladišče MB"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 3000 && recipientCityCode < 4000) || (recipientCityCode >= 6000 && recipientCityCode < 7000))
                        {
                            locationsToParcelIDs["Skladišče KP"].Add(parcelID);
                        }
                        else if ((recipientCityCode >= 7000 && recipientCityCode < 8000) || (recipientCityCode >= 9000 && recipientCityCode < 10000))
                        {
                            locationsToParcelIDs["Skladišče NM"].Add(parcelID);
                        }
                    }

                    int currentBranchID = jobEmployee.BranchID;

                    foreach (String key in locationsToParcelIDs.Keys)
                    {
                        foreach (String parcelID in locationsToParcelIDs[key])
                        {
                            int nextBranchID = 0;
                            int nextJobTypeID = 3;

                            // If current branch is final branch.
                            if (currentBranchID == branchNameToID[key])
                            {
                                nextBranchID = currentBranchID;
                            }
                            // If current branch is not final branch.
                            else if (currentBranchID == 1)
                            {
                                nextBranchID = branchNameToID[key];
                            }

                            List<Staff> staff = _context.Staff.Select(s => s)
                                    .Where(s => (s.BranchID == nextBranchID) && (s.RoleID == 3))
                                    .ToList();
                            Staff driver = staff[new Random().Next(staff.Count)];

                            _context.Add(new Job()
                            {
                                JobTypeID = nextJobTypeID,
                                JobStatusID = 1,
                                StaffUsername = driver.Username,
                                DateCreated = DateTime.Now,
                                ParcelIDs = new List<String>() { parcelID }
                            });
                            _context.SaveChanges();

                            // Link last created job to parcel.
                            _context.JobsParcels.Add(new JobParcel
                            {
                                JobID = _context.Jobs.Select(j => j)
                                            .Where(j => j.JobTypeID == nextJobTypeID && (j.JobStatusID != 3) && j.StaffUsername == driver.Username)
                                            .Select(j => j.ID)
                                            .Max(), // Get last job.
                                ParcelID = parcelID
                            });
                            _context.SaveChanges();

                        }
                    }
                }
                // If delivery cargo confirmation, create job for themselves.
                else if (job.JobTypeID == 7)
                {
                    _context.Add(new Job()
                    {
                        JobTypeID = 8,
                        JobStatusID = 1,
                        StaffUsername = jobEmployee.Username,
                        DateCreated = DateTime.Now,
                        ParcelIDs = job.ParcelIDs
                    });
                    _context.SaveChanges();

                    foreach (String parcelID in job.ParcelIDs)
                    {
                        // Link last created job to parcel.
                        _context.JobsParcels.Add(new JobParcel
                        {
                            JobID = _context.Jobs.Select(j => j)
                                        .Where(j => j.JobTypeID == 8 && j.JobStatusID == 1 && j.StaffUsername == jobEmployee.Username)
                                        .Select(j => j.ID)
                                        .Max(), // Get last job.
                            ParcelID = parcelID
                        });
                        _context.SaveChanges();

                        Parcel p = _context.Parcels.Select(p => p).Where(p => p.ID == parcelID).First();
                        p.ParcelStatusID = 3;
                        _context.Update(p);
                        _context.SaveChanges();
                    }
                }
                else if (job.JobTypeID == 8)
                {
                    foreach (String parcelID in job.ParcelIDs)
                    {
                        Parcel p = _context.Parcels.Select(p => p).Where(p => p.ID == parcelID).First();
                        p.ParcelStatusID = 4;
                        _context.Update(p);
                        _context.SaveChanges();
                    }
                }
            }
        }
    }
}
