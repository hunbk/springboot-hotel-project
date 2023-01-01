package com.ptbh.kyungsunghotel;

import com.ptbh.kyungsunghotel.domain.board.Board;
import com.ptbh.kyungsunghotel.domain.board.BoardRepository;
import com.ptbh.kyungsunghotel.domain.member.Member;
import com.ptbh.kyungsunghotel.domain.member.MemberRepository;
import com.ptbh.kyungsunghotel.domain.reserve.Reserve;
import com.ptbh.kyungsunghotel.domain.reserve.ReserveRepository;
import com.ptbh.kyungsunghotel.domain.room.Room;
import com.ptbh.kyungsunghotel.domain.room.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class KyungsunghotelApplication {


	public static void main(String[] args) {
		SpringApplication.run(KyungsunghotelApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(MemberRepository memberRepository,
									BoardRepository boardRepository,
									RoomRepository roomRepository,
									ReserveRepository reserveRepository) throws Exception {
		return (args) -> {
			memberRepository.save(Member.builder().loginId("test").password("test").name("홍길동").email("test@test.com").telephone("01012345678").build());

			IntStream.rangeClosed(1, 300).forEach(index ->
					boardRepository.save(Board.builder()
							.member(memberRepository.findByLoginId("test").orElse(null))
							.title("게시글 " + index)
							.content("내용" + index)
							.createTime(LocalDateTime.now())
							.build()
					)
			);

			for (int i = 1; i <= 4; i++) {
				for (int j = 1; j <= 4; j++) {
					roomRepository.save(Room.builder().roomNo("A" + i + "0" + j).state("").price(i * 50000).build());
				}
			}

			reserveRepository.save(Reserve.builder()
					.date(LocalDate.now())
					.reserveId(1)
					.room(roomRepository.findById("A101").orElse(null))
					.member(memberRepository.findByLoginId("test").orElse(null))
					.personnel(2)
					.reservePrice(70000)
					.build()
			);
		};
	}
}