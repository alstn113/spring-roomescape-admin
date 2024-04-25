package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fixture.Fixture;

@JdbcTest
class JdbcReservationRepositoryTest {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private ReservationTime savedTime;

    @BeforeEach
    void setUp() {
        savedTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
    }

    @Autowired
    public JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void findAll() {
        // given
        Reservation savedReservation1 = savedReservation("name1", savedTime);
        Reservation savedReservation2 = savedReservation("name2", savedTime);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).containsExactly(savedReservation1, savedReservation2);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void save() {
        // when
        Reservation savedReservation = savedReservation("name", savedTime);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).containsExactly(savedReservation);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteById() {
        // given
        Reservation savedReservation = savedReservation("name", savedTime);

        // when
        reservationRepository.deleteById(savedReservation.getId());

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    private Reservation savedReservation(String name, ReservationTime time) {
        return reservationRepository.save(Fixture.reservation(name, 2024, 4, 21, time));
    }
}
