package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.github.mikesafonov.smpp.server.MockSmppServer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.github.mikesafonov.smpp.assertj.SmppAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class MockSmppServerAssertTest {
    @Nested
    class ReceiveRequestsCount {
        @Test
        void shouldReturnExpectedRequestsCount() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getRequests()).thenReturn(Arrays.asList(new CancelSm()));

            assertNotNull(assertThat(server)
                    .receiveRequestsCount(1));
        }

        @Test
        void shouldFailWhenCountNotMatch() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getRequests()).thenReturn(Arrays.asList(new CancelSm()));

            AssertionError assertionError = assertThrows(AssertionError.class, () -> assertThat(server)
                    .receiveRequestsCount(11));
            assertEquals("Expected messages size to be <11> but was <1>", assertionError.getMessage());
        }

    }

    @Nested
    class HasSingleMessage {
        @Test
        void shouldReturnExpectedAssert() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getSubmitSmMessages()).thenReturn(Collections.singletonList(new SubmitSm()));

            assertNotNull(assertThat(server)
                    .hasSingleMessage());
        }

        @Test
        void shouldFailWhenSizeNotMatch() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getSubmitSmMessages()).thenReturn(Collections.emptyList());

            AssertionError assertionError = assertThrows(AssertionError.class, () -> assertThat(server)
                    .hasSingleMessage());
            assertEquals("Expected messages size to be <1> but was <0>", assertionError.getMessage());
        }
    }

    @Nested
    class HasSingleCancelMessage {
        @Test
        void shouldReturnExpectedAssert() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getCancelSmMessages()).thenReturn(Collections.singletonList(new CancelSm()));

            assertNotNull(assertThat(server)
                    .hasSingleCancelMessage());
        }

        @Test
        void shouldFailWhenSizeNotMatch() {
            MockSmppServer server = mock(MockSmppServer.class);
            when(server.getCancelSmMessages()).thenReturn(Collections.emptyList());

            AssertionError assertionError = assertThrows(AssertionError.class, () -> assertThat(server)
                    .hasSingleCancelMessage());
            assertEquals("Expected messages size to be <1> but was <0>", assertionError.getMessage());
        }
    }

    @Nested
    class Requests {
        @Test
        void shouldReturnExpectedAssert() {
            MockSmppServer server = mock(MockSmppServer.class);

            assertNotNull(assertThat(server)
                    .requests());

            verify(server, times(1)).getRequests();
        }
    }

    @Nested
    class Messages {
        @Test
        void shouldReturnExpectedAssert() {
            MockSmppServer server = mock(MockSmppServer.class);

            assertNotNull(assertThat(server)
                    .messages());

            verify(server, times(1)).getSubmitSmMessages();
        }
    }

    @Nested
    class CancelMessages {
        @Test
        void shouldReturnExpectedAssert() {
            MockSmppServer server = mock(MockSmppServer.class);

            assertNotNull(assertThat(server)
                    .cancelMessages());

            verify(server, times(1)).getCancelSmMessages();
        }
    }
}
