package org.example;

import java.util.List;

import org.example.story.AnomalyType;
import org.example.story.Arthur;
import org.example.story.HeadState;
import org.example.story.ObservationReport;
import org.example.story.SceneAnalyzer;
import org.example.story.TwoHeadedPerson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoryDomainModelTest {

    @DisplayName("<storyDomainModel> Тест #1: Сцена из текста корректно проецируется в доменную модель")
    @Test
    void sceneFromTextIsMappedToDomainModel() {
        TwoHeadedPerson person = TwoHeadedPerson.fromTextScene();

        assertTrue(person.hasTwoHeads());
        assertTrue(person.isReclinedInArmchair());
        assertTrue(person.hasFeetOnControlPanel());
        assertTrue(person.isLeftHandPicksRightHeadTeeth());
        assertEquals(HeadState.SMILING_WIDE_RELAXED, person.leftHeadState());
        assertEquals(HeadState.BUSY_WITH_TEETH_PICKING, person.rightHeadState());
    }

    @DisplayName("<storyDomainModel> Тест #2: Последовательность аномалий совпадает с эталоном")
    @Test
    void anomaliesSequenceMatchesReference() {
        TwoHeadedPerson person = TwoHeadedPerson.fromTextScene();
        SceneAnalyzer analyzer = new SceneAnalyzer();

        ObservationReport report = analyzer.analyze(person);
        List<AnomalyType> expected = List.of(
                AnomalyType.TWO_HEADS,
                AnomalyType.FEET_ON_CONTROL_PANEL,
                AnomalyType.LEFT_HAND_PICKS_RIGHT_HEAD_TEETH,
                AnomalyType.RIGHT_HEAD_BUSY_LEFT_HEAD_SMILING
        );

        assertEquals(expected, report.anomalies());
        assertEquals(4, report.anomalyCount());
    }

    @DisplayName("<storyDomainModel> Тест #3: Артур при наблюдении аномалий ошеломлен и роняет челюсть")
    @Test
    void arthurBecomesAstonishedAndJawDrops() {
        Arthur arthur = new Arthur();
        arthur.enterFollowing();

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(TwoHeadedPerson.fromTextScene());
        arthur.observe(report);

        assertTrue(arthur.isNervous());
        assertTrue(arthur.hasEnteredFollowing());
        assertTrue(arthur.isAstonished());
        assertTrue(arthur.isJawDropped());
        assertEquals(4, arthur.unbelievableThingsCount());
    }

    @DisplayName("<storyDomainModel> Тест #4: Количество невероятных вещей растет при повторных наблюдениях")
    @Test
    void unbelievableThingsCountKeepsGrowing() {
        Arthur arthur = new Arthur();
        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(TwoHeadedPerson.fromTextScene());

        arthur.observe(report);
        assertEquals(4, arthur.unbelievableThingsCount());

        arthur.observe(report);
        assertEquals(8, arthur.unbelievableThingsCount());
    }

    @DisplayName("<storyDomainModel> Тест #5: Если аномалий нет, Артур не ошеломлен и челюсть не отвисает")
    @Test
    void noAnomaliesMeansNoAstonishment() {
        Arthur arthur = new Arthur();
        ObservationReport normalReport = new ObservationReport(List.of());

        arthur.observe(normalReport);

        assertFalse(arthur.isAstonished());
        assertFalse(arthur.isJawDropped());
        assertEquals(0, arthur.unbelievableThingsCount());
    }

    @DisplayName("<storyDomainModel> Тест #6: SceneAnalyzer находит пустой список аномалий при нормальной сцене")
    @Test
    void analyzeNoAnomaliesWhenAllFlagsFalse() {
        TwoHeadedPerson person = new TwoHeadedPerson(
                false,
                true,
                false,
                false,
                HeadState.NEUTRAL,
                HeadState.NEUTRAL
        );

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(person);

        assertTrue(report.anomalies().isEmpty());
        assertEquals(0, report.anomalyCount());
    }

    @DisplayName("<storyDomainModel> Тест #7: Тестирование каждой ветки добавления аномалии TWO_HEADS")
    @Test
    void analyzeOnlyTwoHeadsAddsOnlyTwoHeadsAnomaly() {
        TwoHeadedPerson person = new TwoHeadedPerson(
                true,
                true,
                false,
                false,
                HeadState.NEUTRAL,
                HeadState.NEUTRAL
        );

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(person);

        assertEquals(List.of(AnomalyType.TWO_HEADS), report.anomalies());
    }

    @DisplayName("<storyDomainModel> Тест #8: SceneAnalyzer добавляет RIGHT_HEAD_BUSY_LEFT_HEAD_SMILING только по состояниям голов")
    @Test
    void analyzeCompoundHeadConditionAddsOnlyCompoundAnomaly() {
        TwoHeadedPerson person = new TwoHeadedPerson(
                false,
                true,
                false,
                false,
                HeadState.SMILING_WIDE_RELAXED,
                HeadState.BUSY_WITH_TEETH_PICKING
        );

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(person);

        assertEquals(List.of(AnomalyType.RIGHT_HEAD_BUSY_LEFT_HEAD_SMILING), report.anomalies());
    }

    @DisplayName("<storyDomainModel> Тест #9: RIGHT busy, но left не улыбается — составная аномалия не добавляется")
    @Test
    void analyzeCompoundHeadConditionNotAddedWhenLeftStateNotSmiling() {
        TwoHeadedPerson person = new TwoHeadedPerson(
                false,
                true,
                false,
                false,
                HeadState.NEUTRAL,
                HeadState.BUSY_WITH_TEETH_PICKING
        );

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(person);

        assertTrue(report.anomalies().isEmpty());
        assertEquals(0, report.anomalyCount());
    }

    @DisplayName("<storyDomainModel> Тест #10: Left улыбается, но RIGHT не занята — составная аномалия не добавляется")
    @Test
    void analyzeCompoundHeadConditionNotAddedWhenRightStateNotBusy() {
        TwoHeadedPerson person = new TwoHeadedPerson(
                false,
                true,
                false,
                false,
                HeadState.SMILING_WIDE_RELAXED,
                HeadState.NEUTRAL
        );

        SceneAnalyzer analyzer = new SceneAnalyzer();
        ObservationReport report = analyzer.analyze(person);

        assertTrue(report.anomalies().isEmpty());
        assertEquals(0, report.anomalyCount());
    }
}
