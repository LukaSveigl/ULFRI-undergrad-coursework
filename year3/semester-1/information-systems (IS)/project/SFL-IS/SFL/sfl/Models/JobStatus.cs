using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class JobStatus
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int ID { get; set; }
        [Required]
        [StringLength(45)]
        public string? Name { get; set; }

        public virtual ICollection<Job>? Jobs { get; set; }
    }
}