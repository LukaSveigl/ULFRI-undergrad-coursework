#include <linux/kernel.h>
#include <linux/fs.h>
#include <linux/module.h>
#include <linux/errno.h>
#include <linux/init.h>
#include <linux/uaccess.h>

/** The message buffer size. */
#define BUFSIZE 80
/** The device name. */
#define DEVNAME "charmodule"

// Compile with: sudo make -C /usr/lib/modules/6.0.11-1-MANJARO/build M=$(pwd) modules

/** The device major number. */
static int major_version;
/** Flag that signifies if the device is open. */
static int device_open = 0;

/** The message buffer. */
static char msg[BUFSIZE];
/** The message buffer pointer. */
static char *msg_ptr;

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
    return 0;
}
   
/**
 * Unregisters the current module. 
 */ 
static void __exit cleanup_this_module(void) {
    unregister_chrdev(major_version, DEVNAME);
}

/**
 * Handles the opening of this device, incrementing
 * the internal open counter. Also prepares read message. 
 */
static int open_device(struct inode *inode, struct file *file) {
    static int open_counter = 0;

    if (device_open) {
        return -EBUSY;
    }
    device_open++;

    sprintf(msg, "I had been opened %i-times.\n", open_counter++);
    msg_ptr = msg;

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
 * Handles reading from this device. Only ouputs how many
 * times the device has been opened. 
 */
static ssize_t read_device(struct file *filp, char *buffer, size_t length, loff_t *offset) {
    int bytes_read = 0;

    if (*msg_ptr == 0) {
        return 0;
    }

    while (length && *msg_ptr) {
        put_user(*(msg_ptr++), buffer++);

        length--;
        bytes_read++;
    }
    
    return bytes_read;
}

/**
 * Handles writing to the device. This is an unsupported operation.
 */
static ssize_t write_device(struct file *filp, const char *buffer, size_t length, loff_t *offset) {
    printk("\x1b[31mSorry, the write operation isn't supported.\x1b[0m\n");
    return -EINVAL;
}

// Register init and cleanup functions.
module_init(init_this_module);
module_exit(cleanup_this_module);

// Set the module license. This is so the
// compilation doesn't show a warning.
MODULE_LICENSE("GPL");