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
    public class LogisticsController : Controller
    {
        private readonly CompanyContext _context;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly string[] _navigationProperties;

        public LogisticsController(CompanyContext context, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
            _navigationProperties = new string[] { "ParcelStatus", "RecipientStreet", "SenderStreet", "JobsParcels" };
        }

        // GET: Parcels
        public async Task<IActionResult> Index()
        {
            /*var companyContext = _context.Parcels.Include(p => p.ParcelStatus).Include(p => p.RecipientStreet).Include(p => p.SenderStreet);
            return View(await companyContext.ToListAsync());*/
            return View();
        }
    }
}
