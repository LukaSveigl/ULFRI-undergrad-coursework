using sfl.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;

namespace sfl.Data
{
    public class CompanyContext : IdentityDbContext<ApplicationUser>
    {

        public CompanyContext(DbContextOptions<CompanyContext> options) : base(options)
        {

        }

        public DbSet<Branch> Branches { get; set; }
        public DbSet<City> Cities { get; set; }
        public DbSet<Job> Jobs { get; set; }
        public DbSet<JobStatus> JobStatuses { get; set; }
        public DbSet<JobType> JobTypes { get; set; }
        public DbSet<Parcel> Parcels { get; set; }
        public DbSet<ParcelStatus> ParcelStatuses { get; set; }
        public DbSet<Staff> Staff { get; set; }
        public DbSet<StaffRole> StaffRoles { get; set; }
        public DbSet<Street> Streets { get; set; }
        public DbSet<JobParcel> JobsParcels { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Branch>().ToTable("Branch");
            modelBuilder.Entity<City>().ToTable("City");
            modelBuilder.Entity<Job>().ToTable("Job");
            modelBuilder.Entity<JobStatus>().ToTable("JobStatus");
            modelBuilder.Entity<JobType>().ToTable("JobType");
            modelBuilder.Entity<Parcel>().ToTable("Parcel");
            modelBuilder.Entity<ParcelStatus>().ToTable("ParcelStatus");
            modelBuilder.Entity<Staff>().ToTable("Staff");
            modelBuilder.Entity<StaffRole>().ToTable("StaffRole");
            modelBuilder.Entity<Street>().ToTable("Street");
            modelBuilder.Entity<JobParcel>().ToTable("JobParcel");

            // Set primary keys.
            modelBuilder.Entity<Street>().HasKey(s => new { s.StreetName, s.StreetNumber, s.CityCode });

            // Set foreign keys where automatic generation could not infer them.
            modelBuilder.Entity<Parcel>().HasOne(p => p.SenderStreet)
                .WithMany()
                .HasForeignKey("SenderStreetName", "SenderStreetNumber", "SenderCode")
                .OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Parcel>().HasOne(p => p.RecipientStreet)
                .WithMany(s => s.Parcels)
                .HasForeignKey("RecipientStreetName", "RecipientStreetNumber", "RecipientCode")
                .OnDelete(DeleteBehavior.Restrict);
            modelBuilder.Entity<Street>().HasOne(s => s.Branch)
                .WithOne(b => b.Street)
                .HasForeignKey<Branch>("StreetName", "StreetNumber", "CityCode");
            modelBuilder.Entity<Staff>().HasOne(s => s.Branch)
                .WithMany(b => b.Staff)
                .HasForeignKey(s => s.BranchID);
            modelBuilder.Entity<Staff>().HasOne(s => s.Role)
                .WithMany(r => r.Staff)
                .HasForeignKey(s => s.RoleID);
            modelBuilder.Entity<Job>().HasOne(j => j.JobStatus)
                .WithMany(s => s.Jobs)
                .HasForeignKey(j => j.JobStatusID);
            modelBuilder.Entity<Job>().HasOne(j => j.JobType)
                .WithMany(t => t.Jobs)
                .HasForeignKey(j => j.JobTypeID);


            // Set default values for columns.
            modelBuilder.Entity<Parcel>().Property(p => p.ID).HasDefaultValueSql("SUBSTRING(CONVERT(varchar(50), NEWID()), 1, 8)");
        }
    }
}