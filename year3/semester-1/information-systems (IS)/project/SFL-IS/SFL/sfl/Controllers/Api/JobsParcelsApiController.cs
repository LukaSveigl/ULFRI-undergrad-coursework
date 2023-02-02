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
    [Route("api/v1/JobsParcels")]
    [ApiController]
    public class JobsParcelsApiController : ControllerBase
    {
        private readonly CompanyContext _context;

        public JobsParcelsApiController(CompanyContext context)
        {
            _context = context;
        }

        // GET: api/JobsParcelsApi
        [HttpGet]
        public async Task<ActionResult<IEnumerable<JobParcel>>> GetJobsParcels()
        {
            if (_context.JobsParcels == null)
            {
                return NotFound();
            }
            return await _context.JobsParcels.ToListAsync();
        }

        // GET: api/JobsParcelsApi/5
        [HttpGet("{id}")]
        public async Task<ActionResult<IEnumerable<JobParcel>>> GetJobParcel(int id)
        {
            if (_context.JobsParcels == null)
            {
                return NotFound();
            }
            //var jobParcel = await _context.JobsParcels.FindAsync(jp => jp.jobID == id);

            var jobsParcels = await _context.JobsParcels.Select(jp => jp).Where(jp => jp.JobID == id).ToListAsync();

            if (jobsParcels == null)
            {
                return NotFound();
            }

            return jobsParcels;
        }

        // PUT: api/JobsParcelsApi/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutJobParcel(int id, JobParcel jobParcel)
        {
            if (id != jobParcel.ID)
            {
                return BadRequest();
            }

            _context.Entry(jobParcel).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!JobParcelExists(id))
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

        // POST: api/JobsParcelsApi
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<JobParcel>> PostJobParcel(JobParcel jobParcel)
        {
            if (_context.JobsParcels == null)
            {
                return Problem("Entity set 'CompanyContext.JobsParcels'  is null.");
            }
            _context.JobsParcels.Add(jobParcel);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetJobParcel", new { id = jobParcel.ID }, jobParcel);
        }

        // DELETE: api/JobsParcelsApi/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteJobParcel(int id)
        {
            if (_context.JobsParcels == null)
            {
                return NotFound();
            }
            var jobParcel = await _context.JobsParcels.FindAsync(id);
            if (jobParcel == null)
            {
                return NotFound();
            }

            _context.JobsParcels.Remove(jobParcel);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool JobParcelExists(int id)
        {
            return (_context.JobsParcels?.Any(e => e.ID == id)).GetValueOrDefault();
        }
    }
}
