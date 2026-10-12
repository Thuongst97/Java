# R8 shrinks and obfuscates release builds (lesson 21).
# Room, WorkManager, Material and Gson ship their own consumer rules. Spendly parses JSON with
# Gson's tree API (JsonParser), not reflection, so no model classes need -keep rules.

# Keep line numbers so crash reports from release builds are readable after retracing.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
