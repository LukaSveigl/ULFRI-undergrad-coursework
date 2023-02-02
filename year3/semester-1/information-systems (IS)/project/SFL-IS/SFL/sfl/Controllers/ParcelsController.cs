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
    [Authorize(Roles = "Administrator, Warehouse manager, Logistics agent")]
    public class ParcelsController : Controller
    {
        private readonly CompanyContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly string[] _navigationProperties;

        public ParcelsController(CompanyContext context, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
            _navigationProperties = new string[] { "ParcelStatus", "RecipientStreet", "SenderStreet", "JobsParcels" };
        }

        // GET: Parcels
        public async Task<IActionResult> Index()
        {
            var companyContext = _context.Parcels.Include(p => p.ParcelStatus).Include(p => p.RecipientStreet).Include(p => p.SenderStreet);
            return View(await companyContext.ToListAsync());
        }

        // GET: Parcels/Details/5
        public async Task<IActionResult> Details(string id)
        {
            if (id == null || _context.Parcels == null)
            {
                return NotFound();
            }

            var parcel = await _context.Parcels
                .Include(p => p.ParcelStatus)
                .Include(p => p.RecipientStreet)
                .Include(p => p.SenderStreet)
                .FirstOrDefaultAsync(m => m.ID == id);
            if (parcel == null)
            {
                return NotFound();
            }

            return View(parcel);
        }

        // GET: Parcels/Create
        public IActionResult Create()
        {
            ViewData["ParcelStatusID"] = new SelectList(_context.ParcelStatuses, "ID", "Name");
            ViewData["RecipientCode"] = new SelectList(_context.Cities, "Code", "Code");
            ViewData["SenderCode"] = new SelectList(_context.Cities, "Code", "Code");
            return View();
        }

        // POST: Parcels/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("ID,Weight,Height,Width,Depth,RecipientCode,RecipientStreetName,RecipientStreetNumber,SenderCode,SenderStreetName,SenderStreetNumber")] Parcel parcel)
        {
            RemoveNavigationProperties();
            if (ModelState.IsValid)
            {
                var streetExists = _context.Streets.Any(
                    s => s.CityCode == parcel.SenderCode &&
                    s.StreetName == parcel.SenderStreetName &&
                    s.StreetNumber == parcel.SenderStreetNumber
                );

                if (!streetExists)
                {
                    var street = new Street
                    {
                        CityCode = parcel.SenderCode,
                        StreetName = parcel.SenderStreetName,
                        StreetNumber = parcel.SenderStreetNumber
                    };
                    _context.Streets.Add(street);
                    await _context.SaveChangesAsync();
                }

                streetExists = _context.Streets.Any(
                    s => s.CityCode == parcel.RecipientCode &&
                    s.StreetName == parcel.RecipientStreetName &&
                    s.StreetNumber == parcel.RecipientStreetNumber
                );

                if (!streetExists)
                {
                    var street = new Street
                    {
                        CityCode = parcel.RecipientCode,
                        StreetName = parcel.RecipientStreetName,
                        StreetNumber = parcel.RecipientStreetNumber
                    };
                    _context.Streets.Add(street);
                    await _context.SaveChangesAsync();
                }

                parcel.ParcelStatusID = 1;

                _context.Add(parcel);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ParcelStatusID"] = new SelectList(_context.ParcelStatuses, "ID", "Name", parcel.ParcelStatusID);
            ViewData["RecipientCode"] = new SelectList(_context.Cities, "Code", "Code", parcel.RecipientCode);
            ViewData["SenderCode"] = new SelectList(_context.Cities, "Code", "Code", parcel.SenderCode);
            return View(parcel);
        }

        // GET: Parcels/Edit/5
        public async Task<IActionResult> Edit(string id)
        {
            if (id == null || _context.Parcels == null)
            {
                return NotFound();
            }

            var parcel = await _context.Parcels.FindAsync(id);
            if (parcel == null)
            {
                return NotFound();
            }
            ViewData["ParcelStatusID"] = new SelectList(_context.ParcelStatuses, "ID", "Name", parcel.ParcelStatusID);
            ViewData["RecipientCode"] = new SelectList(_context.Cities, "Code", "Code", parcel.RecipientCode);
            ViewData["SenderCode"] = new SelectList(_context.Cities, "Code", "Code", parcel.SenderCode);
            return View(parcel);
        }

        // POST: Parcels/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(string id, [Bind("ID,Weight,Height,Width,Depth,ParcelStatusID,RecipientCode,RecipientStreetName,RecipientStreetNumber,SenderCode,SenderStreetName,SenderStreetNumber")] Parcel parcel)
        {
            if (id != parcel.ID)
            {
                return NotFound();
            }

            RemoveNavigationProperties();
            if (ModelState.IsValid)
            {
                try
                {
                    //Parcel p = _context.Parcels.Select(p => p).Where(p => p.ID == parcel.ID).FirstOrDefault();

                    //parcel.ParcelStatusID = p.ParcelStatusID;

                    Console.WriteLine(parcel.ParcelStatusID);

                    _context.Update(parcel);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ParcelExists(parcel.ID))
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
            ViewData["ParcelStatusID"] = new SelectList(_context.ParcelStatuses, "ID", "Name", parcel.ParcelStatusID);
            ViewData["RecipientStreetName"] = new SelectList(_context.Streets, "StreetName", "StreetName", parcel.RecipientStreetName);
            ViewData["SenderStreetName"] = new SelectList(_context.Streets, "StreetName", "StreetName", parcel.SenderStreetName);
            return View(parcel);
        }

        // GET: Parcels/Delete/5
        public async Task<IActionResult> Delete(string id)
        {
            if (id == null || _context.Parcels == null)
            {
                return NotFound();
            }

            var parcel = await _context.Parcels
                .Include(p => p.ParcelStatus)
                .Include(p => p.RecipientStreet)
                .Include(p => p.SenderStreet)
                .FirstOrDefaultAsync(m => m.ID == id);
            if (parcel == null)
            {
                return NotFound();
            }

            return View(parcel);
        }

        // POST: Parcels/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(string id)
        {
            if (_context.Parcels == null)
            {
                return Problem("Entity set 'CompanyContext.Parcels'  is null.");
            }
            var parcel = await _context.Parcels.FindAsync(id);
            if (parcel != null)
            {
                _context.Parcels.Remove(parcel);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool ParcelExists(string id)
        {
            return (_context.Parcels?.Any(e => e.ID == id)).GetValueOrDefault();
        }

        private void RemoveNavigationProperties()
        {
            foreach (var key in _navigationProperties)
            {
                ModelState.Remove(key);
            }
        }
    }
}
