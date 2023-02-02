using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class Street
    {
        // These attributes are added as primary key in the OnCreateDatabase.
        [StringLength(150)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string? StreetName { get; set; }
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int StreetNumber { get; set; }

        [Required]
        public string? CityCode { get; set; }
        [Required]
        public virtual City? City { get; set; }
        public virtual Branch? Branch { get; set; }
        public virtual ICollection<Parcel>? Parcels { get; set; }
    }
}