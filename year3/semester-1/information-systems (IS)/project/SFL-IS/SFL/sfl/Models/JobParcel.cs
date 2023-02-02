using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class JobParcel
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }

        [ForeignKey("Parcel")]
        [Required]
        public string? ParcelID { get; set; }
        [Required]
        public virtual Parcel? Parcel { get; set; }

        [ForeignKey("Job")]
        [Required]
        public int JobID { get; set; }
        [Required]
        public virtual Job? Job { get; set; }
    }
}