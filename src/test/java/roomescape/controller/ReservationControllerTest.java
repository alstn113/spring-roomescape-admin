package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.fixture.Fixture;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getAllReservations() throws Exception {
        ReservationTime reservationTime = Fixture.RESERVATION_TIME_1;
        Reservation reservation = Fixture.getReservation(reservationTime);

        BDDMockito.given(reservationService.getAllReservations())
                .willReturn(List.of(ReservationResponse.from(reservation)));

        mockMvc.perform(get("/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].name").value(reservation.getName()))
                .andExpect(jsonPath("$[0].date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$[0].time.id").value(reservationTime.getId()))
                .andExpect(jsonPath("$[0].time.startAt").value(reservationTime.getStartAt().toString()));
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() throws Exception {
        ReservationTime reservationTime = Fixture.RESERVATION_TIME_1;
        Reservation reservation = Fixture.getReservation(reservationTime);
        ReservationRequest reservationRequest = new ReservationRequest(
                reservation.getName(),
                reservation.getDate(),
                reservationTime.getId()
        );

        BDDMockito.given(reservationService.addReservation(any()))
                .willReturn(ReservationResponse.from(reservation));

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/" + reservation.getId()))
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.name").value(reservation.getName()))
                .andExpect(jsonPath("$.date").value(reservation.getDate().toString()))
                .andExpect(jsonPath("$.time.id").value(reservationTime.getId()))
                .andExpect(jsonPath("$.time.startAt").value(reservationTime.getStartAt().toString()));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservationById() throws Exception {
        BDDMockito.willDoNothing()
                .given(reservationService)
                .deleteReservationById(anyLong());

        mockMvc.perform(delete("/reservations/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
