package com.grailslab

class SequenceConstructor {
    protected static List<SequenceConfig> sequenceConfigList = []

    /**
     * construct sequence configuration
     * @param sequenceName unique sequence name (use pattern: PluginKey.Domain.Property)
     * @param sequenceNumber null-able long value and >=0, this sequenceNumber will be available to sequenceScript. If null will not be available. Pass nall if there is not sequential value to maintain
     * @param sequenceScript the groovy script that will execute on generate
     */
    static void constructSequence(String sequenceName, Long sequenceNumber, String sequenceScript) {
        if (sequenceConfigList.find { (it.sequenceName == sequenceName) }) {
            throw new IllegalArgumentException("Sequence name [${sequenceName}] is already registered. Provide unique sequence name.")
        }
        sequenceConfigList.add(new SequenceConfig(sequenceName: sequenceName, sequenceNumber: sequenceNumber, sequenceScript: sequenceScript))
    }

    protected static class SequenceConfig {
        String sequenceName
        Long sequenceNumber
        String sequenceScript
    }
}
