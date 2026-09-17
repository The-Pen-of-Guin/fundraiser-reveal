package com.fundraiser.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.List;

public class FFmpegEncoder implements AutoCloseable {
    private final Process process;
    private final OutputStream stdin;

    public FFmpegEncoder(Path outputFile, int width, int height, int fps) throws IOException {
        Path ffmpegBin = extractBundledFfmpeg();
        List<String> cmd = List.of(
            ffmpegBin.toString(), "-y",
            "-f", "rawvideo", "-pix_fmt", "rgba",
            "-s", width + "x" + height, "-r", String.valueOf(fps),
            "-i", "-",
            "-vf", "vflip",
            "-c:v", "libx264", "-pix_fmt", "yuv420p",
            outputFile.toString()
        );
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT); // swap for a log file if you don't want ffmpeg spam on stdout
        process = pb.start();
        stdin = process.getOutputStream();
    }

    public void writeFrame(ByteBuffer buffer) throws IOException {
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        stdin.write(arr);
    }

    @Override
    public void close() throws IOException {
        stdin.close();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static Path extractBundledFfmpeg() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        String dir, exe;
        if (os.contains("win")) { dir = "win-x64"; exe = "ffmpeg.exe"; }
        else if (os.contains("mac") || os.contains("darwin")) {
            dir = arch.contains("aarch64") || arch.contains("arm") ? "mac-arm64" : "mac-x64";
            exe = "ffmpeg";
        } else {
            dir = arch.contains("aarch64") || arch.contains("arm") ? "linux-arm64" : "linux-x64";
            exe = "ffmpeg";
        }

        String resourcePath = "/ffmpeg/" + dir + "/" + exe;
        try (InputStream in = FFmpegEncoder.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Bundled ffmpeg not found: " + resourcePath);
            Path tempDir = Files.createTempDirectory("gic-ffmpeg");
            Path tempExe = tempDir.resolve(exe);
            Files.copy(in, tempExe, StandardCopyOption.REPLACE_EXISTING);
            if (!os.contains("win")) tempExe.toFile().setExecutable(true);
            tempExe.toFile().deleteOnExit();
            tempDir.toFile().deleteOnExit();
            return tempExe;
        }
    }
}
