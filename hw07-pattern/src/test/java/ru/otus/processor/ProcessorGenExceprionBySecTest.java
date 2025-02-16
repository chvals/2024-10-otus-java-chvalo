package ru.otus.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"java:S1135", "java:S125"})
class ProcessorGenExceprionBySecTest {

    @Test
    void processExceptionTest() {
        var message = new Message.Builder(1L).field8("field8").build();
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        Processor processor = new ProcessorGenExceprionBySec(dateTimeProvider);

        Mockito.when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2025, Month.FEBRUARY,01,12,00, 04));

        assertThatExceptionOfType(ProcessorException.class).isThrownBy(() -> processor.process(message));
    }

    @Test
    void processNotExceptionTest() {
        var message = new Message.Builder(1L).field8("field8").build();
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        Processor processor = new ProcessorGenExceprionBySec(dateTimeProvider);

        Mockito.when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2025, Month.FEBRUARY,01,12,00, 03));

        assertThatNoException().isThrownBy(() -> processor.process(message));
    }
}