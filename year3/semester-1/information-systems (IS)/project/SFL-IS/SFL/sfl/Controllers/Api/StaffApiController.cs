using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using sfl.Data;
using sfl.Models;
using sfl.Filters;

namespace sfl.Controllers_Api
{
    [Route("api/v1/Staff")]
    [ApiController]
    [ApiKeyAuth]
    public class StaffApiController : ControllerBase
    {
        private readonly CompanyContext _context;

        public StaffApiController(CompanyContext context)
        {
            _context = context;
        }

        // GET: api/StaffApi
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Staff>>> GetStaff()
        {
            if (_context.Staff == null)
            {
                return NotFound();
            }
            return await _context.Staff.ToListAsync();
        }

        // GET: api/StaffApi/5
        [HttpGet("{id}")]
        public async Task<ActionResult<IEnumerable<Staff>>> GetStaff(string id)
        {
            if (_context.Staff == null)
            {
                return NotFound();
            }
            //var staff = await _context.Staff.FindAsync(id);
            var branchID = _context.Staff.Select(s => s).Where(s => s.Username == id).FirstOrDefault().BranchID;
            var staff = await _context.Staff.Select(s => s).Where(s => s.BranchID == branchID).ToListAsync();

            if (staff == null)
            {
                return NotFound();
            }

            //staff.Role = _context.StaffRoles.Find(staff.RoleID);

            return staff;
        }

        // PUT: api/StaffApi/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutStaff(string id, Staff staff)
        {
            if (id != staff.Username)
            {
                return BadRequest();
            }

            _context.Entry(staff).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StaffExists(id))
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

        // POST: api/StaffApi
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Staff>> PostStaff(Staff staff)
        {
            if (_context.Staff == null)
            {
                return Problem("Entity set 'CompanyContext.Staff'  is null.");
            }
            _context.Staff.Add(staff);
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateException)
            {
                if (StaffExists(staff.Username))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtAction("GetStaff", new { id = staff.Username }, staff);
        }

        // DELETE: api/StaffApi/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteStaff(string id)
        {
            if (_context.Staff == null)
            {
                return NotFound();
            }
            var staff = await _context.Staff.FindAsync(id);
            if (staff == null)
            {
                return NotFound();
            }

            _context.Staff.Remove(staff);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool StaffExists(string id)
        {
            return (_context.Staff?.Any(e => e.Username == id)).GetValueOrDefault();
        }
    }
}
