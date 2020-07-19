package com.dangleterre.thomas.movies.services;

import com.dangleterre.thomas.movies.beans.TopBean;
import com.dangleterre.thomas.movies.entities.MovieEntity;
import com.dangleterre.thomas.movies.repositories.CommentRepository;
import com.dangleterre.thomas.movies.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MovieRepository movieRepository;

    public List<TopBean> getTops(Long dateBegin, Long dateEnd) {
        List<MovieEntity> listMoviesInRange = movieRepository.findAll().stream()
                .filter(movieEntity -> movieEntity.getYearBegin() >= dateBegin && movieEntity.getYearEnd() <= dateEnd)
                .collect(Collectors.toList());
        ArrayList<TopBean> listTopBean = listMoviesInRange.stream().map(movieEntity -> {
                    TopBean topBean = new TopBean();
                    topBean.setMovieId(movieEntity.getId());
                    topBean.setTotalComments(commentRepository.countByMovieId(movieEntity.getId()));
                    return topBean;
                }
        )
                .sorted(Comparator.comparing(TopBean::getTotalComments).reversed())
                .collect(Collectors.toCollection(ArrayList::new));

        long rank = 1;
        TopBean previous = new TopBean();
        previous.setTotalComments(-1L);

        for (TopBean topBean : listTopBean) {
            if (topBean.getTotalComments().equals(previous.getTotalComments())) {
                topBean.setRank(previous.getRank());
            } else {
                topBean.setRank(rank);
            }
            rank++;
            previous = topBean;
        }
        return listTopBean;
    }
}
