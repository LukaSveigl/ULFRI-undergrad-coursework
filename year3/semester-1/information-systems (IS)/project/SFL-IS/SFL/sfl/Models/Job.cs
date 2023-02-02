using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class Job
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        [Required]
        public DateTime DateCreated { get; set; }
        // Can be null.
        public DateTime? DateCompleted { get; set; }

        [Required]
        public virtual int JobStatusID { get; set; }
        [Required]
        public virtual JobStatus? JobStatus { get; set; }

        [Required]
        public virtual int JobTypeID { get; set; }
        [Required]
        public virtual JobType? JobType { get; set; }

        [Required]
        public virtual string? StaffUsername { get; set; }
        [Required]
        public virtual Staff? Staff { get; set; }

        [Required]
        public virtual ICollection<JobParcel>? JobsParcels { get; set; }

        [NotMapped]
        public ICollection<string> ParcelIDs = new List<string> { };
    }
}