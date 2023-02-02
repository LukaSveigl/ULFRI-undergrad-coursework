# Read sound files
[data_by, sample_rate_by]   = audioread('data/born_yesterday.wav');
[data_dwd, sample_rate_dwd] = audioread('data/danced_with_devil.wav');
[data_fs, sample_rate_fs]   = audioread('data/Female_scream.wav'); 
[data_ms, sample_rate_ms]   = audioread('data/Man_scream.wav');   

# Load needed packages
pkg load signal

function ret = generate_stft(data, sample_rate, fignum)
  # Get size of data window (20ms)
  window = fix((20 * sample_rate) / 1000);
  # Create new figure for spectrogram
  figure(fignum);
  # Display spectrogram of data, with segment length n,
  # sample_rate and window size
  #specgram(data, n, sample_rate, window);
  specgram(data)
  # Calculate short-time fourier transform and return
  y = stft(data, window);
  ret = y;
endfunction

# Display spectrogram for each recording
f_fs  = generate_stft(data_fs, sample_rate_fs, 1);
f_ms  = generate_stft(data_ms, sample_rate_ms, 2);
f_by  = generate_stft(data_by, sample_rate_by, 3);
f_dwd = generate_stft(data_dwd, sample_rate_dwd, 4);

s1 = mean(f_fs);
s2 = mean(f_ms);
s3 = mean(f_by);
s4 = mean(f_dwd);

# Plot the STFT results calculated by the generate_stft function
# by hand (not plotting a spectrogram).
figure(5)
subplot(2, 2, 1);
plot((0 : length(s1) - 1), s1);
title('Female scream');
figure(5)
subplot(2, 2, 2);
plot((0 : length(s2) - 1), s2);
title('Male scream');
figure(5)
subplot(2, 2, 3);
plot((0 : length(s3) - 1), s3);
title('Born yesterday');
figure(5)
subplot(2, 2, 4);
plot((0 : length(s4) - 1), s4);
title('Danced with devil');
