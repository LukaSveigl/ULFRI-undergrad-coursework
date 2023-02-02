using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace sfl.Models
{
    [NotMapped]
    public class ApiLoginModel
    {
        [NotMapped]
        public string Username { get; set; }
        [NotMapped]
        public string Password { get; set; }
    }
}