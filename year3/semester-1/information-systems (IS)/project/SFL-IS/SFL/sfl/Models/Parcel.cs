using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class Parcel
    {
        [Key]
        [StringLength(8)]
        public string? ID { get; set; }
        [Required]
        public double Weight { get; set; }
        [Required]
        public int Height { get; set; }
        [Required]
        public int Width { get; set; }
        [Required]
        public int Depth { get; set; }

        [Required]
        public int ParcelStatusID { get; set; }
        [Required]
        public virtual ParcelStatus? ParcelStatus { get; set; }

        [ForeignKey("RecipientStreet")]
        [Required]
        public string? RecipientCode { get; set; }
        [Required]
        public string? RecipientStreetName { get; set; }
        [Required]
        public int RecipientStreetNumber { get; set; }
        [Required]
        public virtual Street? RecipientStreet { get; set; }

        [ForeignKey("SenderStreet")]
        [Required]
        public string? SenderCode { get; set; }
        [Required]
        public string? SenderStreetName { get; set; }
        [Required]
        public int SenderStreetNumber { get; set; }
        [Required]
        public virtual Street? SenderStreet { get; set; }

        public virtual ICollection<JobParcel>? JobsParcels { get; set; }
    }
}