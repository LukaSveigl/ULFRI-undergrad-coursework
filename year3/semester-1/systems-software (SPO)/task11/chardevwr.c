#include <linux/kernel.h>
#include <linux/fs.h>
#include <linux/module.h>
#include <linux/errno.h>
#include <linux/init.h>
#include <linux/uaccess.h>
#include <linux/slab.h>
#include <linux/moduleparam.h>

/** The device name. */
#define DEVNAME "chardevwr"

// Compile with: sudo make -C /usr/lib/modules/6.0.11-1-MANJARO/build M=$(pwd) modules

/** The device major number. */
static int major_version;
/** Flag that signifies if the device is open. */
static int device_open = 0;


/** The default memory size. Can be overwritten on init. */
static int MEMSIZE = 1024;
/** The pointer used to write data. */
static char *write_ptr;
/** The size of data. */
static int size = 0;

/*
 * Module parameter definitions.
 */
module_param(MEMSIZE, int, S_IRUGO);
MODULE_PARM_DESC(MEMSIZE, "The write memory size.");

/*
 * Function prototypes.
 */
static int open_device(struct inode *, struct file *);
static int release_device(struct inode *, struct file *);
static ssize_t read_device(struct file *, char *, size_t, loff_t *);
static ssize_t write_device(struct file *, const char *, size_t, loff_t *);

/** Struct that handles registering file operations of module.*/
static struct file_operations fops = {
    .read = read_device,
    .write = write_device,
    .open = open_device,
    .release = release_device
};

/**
 * Initializes the current module with random major version. 
 */
static int __init init_this_module(void) {
    major_version = register_chrdev(0, DEVNAME, &fops);

    if (major_version < 0) {
        printk("Registering the character device failed with %d\n", major_version);
        return major_version;
    }

    write_ptr = kmalloc(MEMSIZE, GFP_KERNEL);
    
    if (write_ptr == NULL) {
        printk("Initializing the required memory failed.\n");
        return -ENOMEM;
    } 

    write_ptr = memset(write_ptr, 0, MEMSIZE);

    return 0;
}
   
/**
 * Unregisters the current module. 
 */ 
static void __exit cleanup_this_module(void) {
    unregister_chrdev(major_version, DEVNAME);

    if (write_ptr != NULL) {
        kfree(write_ptr);
    }
}

/**
 * Handles the opening of this device, incrementing
 * the internal open counter. Also prepares read message. 
 */
static int open_device(struct inode *inode, struct file *file) {
    if (device_open) {
        return -EBUSY;
    }
    device_open++;

    return 0;
}

/**
 * Handles the closing of this device. Only decrements the 
 * flag that signifies if the device is open. 
 */
static int release_device(struct inode *inode, struct  file *file) {
    device_open--;
    return 0;
}

/**
 * Handles reading from this device.
 */
static ssize_t read_device(struct file *filp, char *buffer, size_t length, loff_t *offset) {

    // If at end, do not read.
    if (*offset >= size) {
        return 0;
    }

    // Calculate how much should be read.
    if (length > size - *offset) {
        length = size - *offset;
    }

    // Copy to user memory space.
    if (copy_to_user(buffer, write_ptr, length)) {
        return -EFAULT;
    }

    // Move offset forward.
    *offset += length;
    return length;
}

/**
 * Handles writing to the device.
 */
static ssize_t write_device(struct file *filp, const char *buffer, size_t length, loff_t *offset) {

    if (*offset > MEMSIZE) {
        return 0;
    }

    if (length > MEMSIZE - size) {
        length = MEMSIZE - size;
    }

    if (length == 0) {
        return 0;
    }

    if (copy_from_user((write_ptr + size), buffer, length)) {
        return -EFAULT;
    }

    size += length;
    *offset += length;
    return size;
}

// Register init and cleanup functions.
module_init(init_this_module);
module_exit(cleanup_this_module);

// Set the module license. This is so the
// compilation doesn't show a warning.
MODULE_LICENSE("GPL");