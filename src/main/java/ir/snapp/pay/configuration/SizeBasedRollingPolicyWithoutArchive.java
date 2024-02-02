package ir.snapp.pay.configuration;

import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RolloverFailure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SizeBasedRollingPolicyWithoutArchive extends FixedWindowRollingPolicy {

    @Override
    public void rollover() throws RolloverFailure {
        String activeFileName = getActiveFileName();
        try {
            boolean isDeleted = Files.deleteIfExists(Path.of(activeFileName));
            if (!isDeleted)
                throw new IOException("Failed to delete log file: " + activeFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RolloverFailure("Roll over the log file failed!", e);

        }
    }
}