using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class City
    {
        [Key]
        [StringLength(20)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string? Code { get; set; }
        [Required]
        [StringLength(145)]
        public string? Name { get; set; }

        public virtual ICollection<Street>? Streets { get; set; }
    }
}