using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class Branch
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        [Required]
        [StringLength(45)]
        public string? Name { get; set; }

        public string? CityCode { get; set; }
        public string? StreetName { get; set; }
        public int StreetNumber { get; set; }

        public virtual Street? Street { get; set; }
        [Required]
        public virtual ICollection<Staff>? Staff { get; set; }
    }
}