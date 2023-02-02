using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    public class Staff
    {
        [Key]
        [StringLength(128)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public string? Username { get; set; }
        [Required]
        [StringLength(85)]
        public string? Name { get; set; }
        [Required]
        [StringLength(85)]
        public string? Surname { get; set; }

        [Required]
        [ForeignKey("BranchID")]
        public int BranchID { get; set; }
        [Required]
        public virtual Branch? Branch { get; set; }
        public virtual ICollection<Job>? Jobs { get; set; }
        [Required]
        [ForeignKey(name: "RoleID")]
        public int RoleID { get; set; }
        [Required]
        public virtual StaffRole? Role { get; set; }

        [NotMapped]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} and at max {1} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "Password")]
        public string? Password { get; set; }
        [NotMapped]
        [DataType(DataType.Password)]
        [Display(Name = "Confirm password")]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string? ConfirmPassword { get; set; }
        [NotMapped]
        public string? PhoneNumber { get; set; }
    }
}