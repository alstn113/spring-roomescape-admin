package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.fixture.Fixture;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약 시간들을 조회한다.")
    void findAll() {
        // given
        ReservationTime savedTime1 = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        ReservationTime savedTime2 = reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);

        // when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        // then
        assertThat(reservationTimes).containsExactly(savedTime1, savedTime2);
    }

    @Test
    @DisplayName("예약 시간을 조회한다.")
    void findById() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        // when
        Optional<ReservationTime> findTime = reservationTimeRepository.findById(savedTime.getId());

        // then
        assertThat(findTime).isPresent().contains(savedTime);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void save() {
        // when
        ReservationTime savedTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        // then
        List<ReservationTime> time = reservationTimeRepository.findAll();
        assertThat(time).containsExactly(savedTime);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteById() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        // when
        reservationTimeRepository.deleteById(savedTime.getId());

        // then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes).isEmpty();
    }
}
