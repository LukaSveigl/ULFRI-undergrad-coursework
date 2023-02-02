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
    [Route("api/v1/Parcels")]
    [ApiController]
    public class ParcelsApiController : ControllerBase
    {
        private readonly CompanyContext _context;

        public ParcelsApiController(CompanyContext context)
        {
            _context = context;
        }

        // GET: api/ParcelsApi
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Parcel>>> GetParcels()
        {
            if (_context.Parcels == null)
            {
                return NotFound();
            }
            return await _context.Parcels.ToListAsync();
        }

        // GET: api/ParcelsApi/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Parcel>> GetParcel(string id)
        {
            if (_context.Parcels == null)
            {
                return NotFound();
            }
            var parcel = await _context.Parcels.FindAsync(id);

            if (parcel == null)
            {
                return NotFound();
            }

            return parcel;
        }

        // PUT: api/ParcelsApi/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutParcel(string id, Parcel parcel)
        {
            if (id != parcel.ID)
            {
                return BadRequest();
            }

            _context.Entry(parcel).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ParcelExists(id))
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

        // POST: api/ParcelsApi
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Parcel>> PostParcel(Parcel parcel)
        {
            if (_context.Parcels == null)
            {
                return Problem("Entity set 'CompanyContext.Parcels'  is null.");
            }
            _context.Parcels.Add(parcel);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetParcel", new { id = parcel.ID }, parcel);
        }

        // DELETE: api/ParcelsApi/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteParcel(string id)
        {
            if (_context.Parcels == null)
            {
                return NotFound();
            }
            var parcel = await _context.Parcels.FindAsync(id);
            if (parcel == null)
            {
                return NotFound();
            }

            _context.Parcels.Remove(parcel);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool ParcelExists(string id)
        {
            return (_context.Parcels?.Any(e => e.ID == id)).GetValueOrDefault();
        }
    }
}
