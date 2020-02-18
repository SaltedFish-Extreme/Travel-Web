package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    public PageBean<Route> pageQuery(int cid, int currentPage, int PageSize, String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(PageSize);
        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * PageSize;
        List<Route> list = routeDao.findByPage(cid, start, PageSize, rname);
        pb.setList(list);
        //设置总页数=总记录数/每页显示条数
        int totalPage = totalCount % PageSize == 0 ? totalCount / PageSize : (totalCount / PageSize) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public Route find(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.find(Integer.parseInt(rid));
        //2.根据id查询图片整合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //3.将集合设置到route对象中
        route.setRouteImgList(routeImgList);
        //4.根据route的sid(商家id)查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //5.查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
