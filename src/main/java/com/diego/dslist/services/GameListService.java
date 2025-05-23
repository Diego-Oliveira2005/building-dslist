package com.diego.dslist.services;

import com.diego.dslist.dto.GameListDTO;
import com.diego.dslist.entities.GameList;
import com.diego.dslist.repositories.gameListRepository;
import com.diego.dslist.repositories.gameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projections.GameMinProjection;

import java.util.List;

@Service
public class GameListService {

    @Autowired
    private gameListRepository gameListRepository;

    @Autowired
    private gameRepository gameRepository;

    @Transactional(readOnly = true)
    public List<GameListDTO> findAll() {
        List<GameList> result = gameListRepository.findAll();
        return result.stream().map(x -> new GameListDTO(x)).toList();
    }

    @Transactional
    public void move(Long listId, int sourceIndex, int destinationIndex) {

        List<GameMinProjection> list = gameRepository.searchByList(listId);

        GameMinProjection obj = list.remove(sourceIndex);
        list.add(destinationIndex, obj);

        int min = sourceIndex < destinationIndex ? sourceIndex : destinationIndex;
        int max = sourceIndex < destinationIndex ? destinationIndex : sourceIndex;

        for (int i = 0; i <= max; i++) {
            gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
        }
    }
}
