package sample.cafekiosk.spring.api.service.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

//    @Spy
    @Mock
    MailSendClient mailSendClient;

    @Mock
    MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    MailService mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void test(){
        //given
//        Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true);

        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        /**
        @Spy : 객체의 특정 메서드만 스터빙을 하고 나머지 메서드들을 실제 동작을 기대하게 할 수 있는 어노테이션
               doReturn -> true를 무조건 적으로 반환한다.
               when -> mailSendClient 객체를 스터빙 할것이다.
               method -> mailSendClient의 sendEmail만 Stubbing을 하고
               나머지 메서드들은 스터빙을 하지 않고 실제로 동작한다
        **/

//        Mockito.doReturn(true)
//                .when(mailSendClient)
//                .sendEmail(anyString(), anyString(), anyString(), anyString());

        //when
        boolean result = mailService.sendMail("", "", "", "");

        verify(mailSendHistoryRepository, times(1))
                .save(any(MailSendHistory.class));

        //then
        Assertions.assertThat(result).isTrue();
    }

}