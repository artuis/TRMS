package com.revature.data.cass;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Player;
import com.revature.beans.Player.Role;
import com.revature.data.PlayerDao;

@Service
public class PlayerDaoCass implements PlayerDao{
	@Autowired
	private CqlSession session;
	
	@Override
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		
		String query = "select name, role, score from player";
		ResultSet rs = session.execute(query);
		
		rs.forEach(data -> {
			Player p = new Player();
			p.setName(data.getString("name"));
			p.setRole(Role.valueOf(data.getString("role")));
			p.setScore(data.getInt("score"));
			players.add(p);
		});
		
		return players;
	}

	@Override
	public Player getPlayerByName(String name) {
		Player p = null;
		String query = "Select name, role, score from player where name = ?;";
		BoundStatement bound = session.prepare(query).bind(name);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			p = new Player();
			p.setName(data.getString("name"));
			p.setRole(Role.valueOf(data.getString("role")));
			p.setScore(data.getInt("score"));
		}
		return p;
	}

	@Override
	public void addPlayer(Player p) throws Exception {
		String query = "Insert into player (name, role, score) values (?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(p.getName(), p.getRole().toString(), p.getScore());
		session.execute(bound);
	}

	@Override
	public void updatePlayer(Player p) {
		String query = "update player set role = ?, score = ? where name = ?";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(p.getRole().toString(), p.getScore(), p.getName());
		session.execute(bound);
	}

}
