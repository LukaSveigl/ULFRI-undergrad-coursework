# Read needed images at the start
baboon  = imread("images/baboon_bl.png");
barbara = imread("images/barbara_bl.png");
lenna   = imread("images/lenna_bl.png");
monarch = imread("images/monarch_bl.png");
teeth   = imread("images/teeth_bl.png");


## First approach: highboost filtering        

# Define function to sharpen image using highboost filtering
function sharpened = highboost_filter (image, A)
  # Convolution matrix 
  kernel = [[1/9, 1/9, 1/9],
           [1/9,  1/9, 1/9],
           [1/9,  1/9, 1/9]];
           
   # Blur image using kernel
   blur = conv2(image, kernel, "same");
   # Calculate highpass
   highpass = image - blur;
   # Sharpen image
   sharpened = image + A * highpass;
endfunction  
  
imwrite(highboost_filter(baboon,  1), "images/highboost/baboon_bl_sharp.png");
imwrite(highboost_filter(barbara, 1), "images/highboost/barbara_bl_sharp.png");
imwrite(highboost_filter(lenna,   1), "images/highboost/lenna_bl_sharp.png");
imwrite(highboost_filter(monarch, 1), "images/highboost/monarch_bl_sharp.png");
imwrite(highboost_filter(teeth,   1), "images/highboost/teeth_bl_sharp.png");

           
## Second approach: using the second order derivative of an image

laplacian_mask = [[1,  1, 1],
                  [1, -8, 1], 
                  [1,  1, 1]];

function sharpened = scnd_der_sharpen(image, laplacian_mask)
  # Apply laplacian mask on entire image in order to get
  # the second order derivative, which is defined as:
  # f"(x, y) = f(x+1,y) + f(x-1,y) + f(x,y+1) + f(x,y-1) - 4f(x,y)
  # for mask [0, 1, 0; 1, -4, 1; 0, 1, 0] or as:
  # f"(x, y) = f(x+1,y) + f(x-1,y) + f(x,y+1) + f(x,y-1) + f(x-1,y-1) +
  # f(x+1,y-1) + f(x-1,y+1) + f(x+1,y+1) - 8f(x,y) 
  # for mask [1, 1, 1; 1, -8, 1; 1, 1, 1]
  convolved = conv2(image, laplacian_mask, "same");
  # Normalize the pixel range to 0-255
  normalized = uint8(convolved);
  # Sharpen the image using equation:
  # g(x,y) = f(x,y) - f"(x,y) if the center of laplacian mask is negative
  # or 
  # g(x,y) = f(x,y) + f"(x,y) if the center of laplacian mask is positive
  if (laplacian_mask(2,2) < 0);
    sharpened = image - normalized;
  else 
    sharpened = image + normalized;
  endif
endfunction

imwrite(scnd_der_sharpen(baboon, laplacian_mask), 
        "images/second_derivative/baboon_bl_sharp.png");
imwrite(scnd_der_sharpen(barbara, laplacian_mask), 
        "images/second_derivative/barbara_bl_sharp.png");
imwrite(scnd_der_sharpen(lenna, laplacian_mask), 
        "images/second_derivative/lenna_bl_sharp.png");
imwrite(scnd_der_sharpen(monarch, laplacian_mask), 
        "images/second_derivative/monarch_bl_sharp.png");
imwrite(scnd_der_sharpen(teeth, laplacian_mask), 
        "images/second_derivative/teeth_bl_sharp.png");        
        
        
# Run GUI to select image

filepath = fullfile("images/", uigetfile("images/"));
img = imread(filepath);

figure, imshow(img)
figure, imshow(highboost_filter(img, 1))
figure, imshow(scnd_der_sharpen(img, laplacian_mask))

