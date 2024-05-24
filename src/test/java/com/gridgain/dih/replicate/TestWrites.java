package com.gridgain.dih.replicate;

import java.sql.Timestamp;

import com.gridgain.gen.app.IgniteClientHelper;
import com.gridgain.gen.model.Astronauts;
import com.gridgain.gen.model.City;
import com.gridgain.gen.model.Members;
import com.gridgain.gen.model.Theaters;

public class TestWrites {

	public static void main(String[] args) throws Exception {
		new TestWrites();
	}

	public TestWrites() throws Exception {
		try (IgniteClientHelper ich = new IgniteClientHelper(true)) {
			testCassandraWrites(ich);
			testMysqlWrites(ich);
			testPostgresWrites(ich);
			testMongoDBWrites(ich);
		}
	}

	public void testCassandraWrites(IgniteClientHelper ich) throws Exception {
		Astronauts astronaut = new Astronauts("Starfleet Academy", "Riverside, Iowa", "2233", "Male", 99,
				"To boldly go where no man has gone before", "James Kirk", 20 * 365 * 24, 100, 0, 0, "Retired", 2233);
		ich.getAstronautsCache().put(999, astronaut);
	}

	public void testMysqlWrites(IgniteClientHelper ich) throws Exception {
		City city = new City(9999, "Marlow", "GBR", "England", 14325);
		ich.getCityCache().put(city.getId(), city);
	}

	public void testPostgresWrites(IgniteClientHelper ich) throws Exception {
		Members member = new Members(999, "Blogs", "Fred", "Buckingham Palace", 12345, "0208123456", 0,
				new Timestamp(System.currentTimeMillis()));
		ich.getMembersCache().put(member.getMemid(), member);
	}

	public void testMongoDBWrites(IgniteClientHelper ich) throws Exception {
		Theaters theater = new Theaters("59a47287cfa9a3a73e51ffff", 9999,
				"{\"address\":{\"street1\":\"1 High Street\",\"city\":\"Marlow\",\"state\":\"Bucks\",\"zipcode\":\"SL7 2ZZ\"},\"geo\":{\"type\":\"Point\",\"coordinates\":[-90.057671,32.344334]}}");
		ich.getTheatersCache().put(theater.getId(), theater);
	}

}
