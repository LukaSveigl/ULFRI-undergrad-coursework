using Microsoft.AspNetCore.Identity;

namespace sfl.Models
{
    public class ApplicationUser : IdentityUser
    {
        public string? Name;
        public string? Surname;
    }
}