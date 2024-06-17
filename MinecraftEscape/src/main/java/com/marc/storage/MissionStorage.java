package com.marc.storage;

import java.util.Map;
import java.util.UUID;

public interface MissionStorage {
    void saveMissionProgress(UUID playerId, Map<String, Boolean> progress);
    Map<String, Boolean> loadMissionProgress(UUID playerId);
}
