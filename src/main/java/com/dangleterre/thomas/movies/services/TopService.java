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

    /**
     * Get top commented films present in data range
     * @param dateBegin filter year minimun
     * @param dateEnd filter year maximun
     * @return a list of top bean
     */
    public List<TopBean> getTops(Long dateBegin, Long dateEnd) {
        // Fecth all movies in the range dateBegin - dateEnd
        List<MovieEntity> listMoviesInRange = movieRepository.findAll().stream()
                .filter(movieEntity -> movieEntity.getYearBegin() >= dateBegin && movieEntity.getYearEnd() <= dateEnd)
                .collect(Collectors.toList());

        //Fill top bean with movie id and total number of comments and sort them
        ArrayList<TopBean> listTopBean= fillTopListBean(listMoviesInRange);

        //Fill rank of top bean and return then
        return setRankTopListBean(listTopBean);
    }

    /**
     * Fill movie with movie id and corresponding number of comments
     * and sort them by descending number of number of comments
     * @param listMovies
     * @return sorted top bean list
     */
    public ArrayList<TopBean> fillTopListBean(List<MovieEntity> listMovies){
        return listMovies.stream().map(movieEntity -> {
                    TopBean topBean = new TopBean();
                    topBean.setMovieId(movieEntity.getId());
                    topBean.setTotalComments(commentRepository.countByMovieId(movieEntity.getId()));
                    return topBean;
                }
        )
                .sorted(Comparator.comparing(TopBean::getTotalComments).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Set the rank of a list of top bean
     * 2 beans with same number of total comments get the same rank
     * @param listTopBean
     * @return list of top bean with rank setted
     */
    public ArrayList<TopBean> setRankTopListBean(ArrayList<TopBean> listTopBean){

        long rank = 1;
        TopBean previous = new TopBean();
        previous.setTotalComments(-1L);
        // Loop over each topBean
        for (TopBean topBean : listTopBean) {
            // if a bean has the same number of comments
            if (topBean.getTotalComments().equals(previous.getTotalComments())) {
                // so it has the same rank
                topBean.setRank(previous.getRank());
            } else {
                // else it gets the current rank
                topBean.setRank(rank);
            }
            rank++;
            previous = topBean;
        }
        return listTopBean;
    }
}
