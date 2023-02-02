using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using sfl.Data;
using sfl.Models;

namespace sfl.Controllers_Api
{
    [Route("api/v1/Jobs")]
    [ApiController]
    public class JobsApiController : ControllerBase
    {
        private readonly CompanyContext _context;

        public JobsApiController(CompanyContext context)
        {
            _context = context;
        }

        // GET: api/JobsApi
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Job>>> GetJobs()
        {
            if (_context.Jobs == null)
            {
                return NotFound();
            }
            return await _context.Jobs.ToListAsync();
        }

        // GET: api/JobsApi/5
        /*[HttpGet("{id}")]
        public async Task<ActionResult<Job>> GetJob(int id)
        {
            if (_context.Jobs == null)
            {
                return NotFound();
            }
            var job = await _context.Jobs.FindAsync(id);

            if (job == null)
            {
                return NotFound();
            }

            return job;
        }*/

        [HttpGet("{username}")]
        public async Task<ActionResult<IEnumerable<Job>>> GetJob(string username)
        {
            if (_context.Jobs == null)
            {
                return NotFound();
            }
            var jobs = await _context.Jobs.Select(j => j).Where(j => j.StaffUsername == username).ToListAsync();

            /*foreach (var job in jobs)
            {
                job.ParcelIDs = _context.JobsParcels.Select(pj => pj).Where(pj => pj.JobID == job.ID).Select(pj => pj.ParcelID).ToList();
                job.JobsParcels = _context.JobsParcels.Select(pj => pj).Where(pj => pj.JobID == job.ID).ToList();
            }*/

            if (jobs == null)
            {
                return NotFound();
            }

            return jobs;
        }

        // PUT: api/JobsApi/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}/{jobStatusID}")]
        public async Task<IActionResult> PutJob(int id, int jobStatusID)//Job job)
        {
            var job = await _context.Jobs.Select(j => j).Where(j => j.ID == id).FirstOrDefaultAsync();

            if (job == null)
            {
                return NotFound();
            }

            if (id != job.ID)
            {
                return BadRequest();
            }

            if (job.JobStatusID == jobStatusID)
            {
                return NoContent();
            }

            if (jobStatusID == 2)
            {
                job.DateCompleted = DateTime.Now;
            }

            job.JobStatusID = jobStatusID;
            _context.Update(job);

            try
            {
                await _context.SaveChangesAsync();
                MoveJob(job);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!JobExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/JobsApi
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        /*[HttpPost]
        public async Task<ActionResult<Job>> PostJob(Job job)
        {
            if (_context.Jobs == null)
            {
                return Problem("Entity set 'CompanyContext.Jobs'  is null.");
            }

            _context.Jobs.Add(job);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetJob", new { id = job.ID }, job);
        }*/

        [HttpPost]
        public async Task<ActionResult<PostRq>> PostJob(PostRq job)
        {
            if (_context.Jobs == null)
            {
                return Problem("Entity set 'CompanyContext.Jobs'  is null.");
            }

            var actualJob = new Job
            {
                StaffUsername = job.StaffUsername,
                JobStatusID = 1,
                JobTypeID = int.Parse(job.JobTypeID),
                DateCreated = DateTime.Now,
                DateCompleted = null,
                ParcelIDs = job.ParcelIDs
            };

            _context.Jobs.Add(actualJob);
            await _context.SaveChangesAsync();

            for (int i = 0; i < actualJob.ParcelIDs.Count; i++)
            {
                var jobParcel = new JobParcel
                {
                    JobID = actualJob.ID,
                    ParcelID = job.ParcelIDs[i],
                };

                _context.JobsParcels.Add(jobParcel);
                await _context.SaveChangesAsync();
            }

            return CreatedAtAction("GetJob", new { id = actualJob.ID }, job);
        }

        // DELETE: api/JobsApi/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteJob(int id)
        {
            if (_context.Jobs == null)
            {
                return NotFound();
            }
            var job = await _context.Jobs.FindAsync(id);
            if (job == null)
            {
                return NotFound();
            }

            _context.Jobs.Remove(job);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool JobExists(int id)
        {
            return (_context.Jobs?.Any(e => e.ID == id)).GetValueOrDefault();
        }

        private void MoveJob(Job job)
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

    public class PostRq
    {
        public string StaffUsername { get; set; }
        public string JobTypeID { get; set; }
        public string[] ParcelIDs { get; set; }
    }
}
