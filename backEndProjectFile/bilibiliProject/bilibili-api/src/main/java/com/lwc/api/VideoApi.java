package com.lwc.api;

import com.lwc.api.support.UserSupport;
import com.lwc.domain.*;
import com.lwc.service.VideoService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * ClassName: VideoApi
 * Description:
 *
 * @Author 林伟朝
 * @Create 2024/10/22 15:16
 */
@RestController
public class VideoApi {
    @Autowired
    private VideoService videoService;

    @Autowired
    private UserSupport userSupport;





    //分页查询加载视频流,输入每页大小和第几页,以及要展示的分区,然后去数据库查
    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size, Integer no, String area) {
        PageResult<Video>result=videoService.pageListVideos(size, no, area);
        return new JsonResponse<>(result);

    }

    //在线浏览观看视频接口
    /*文件分片下载，后台直接返回存储服务器文件的完整http请求路径的话，不安全，会被白嫖，客户端只能知道相对路径，由后台来包装并帮助客户端
    发起文件下载请求并写入输出流返回给前端*/
    @GetMapping("video-slices")
    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String url) throws Exception {//客户端至多只能知道资源在服务器上的相对路径，完整的请求由后台发起
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    /*点赞视频*/
    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    /*取消点赞*/
    @DeleteMapping("/video-likes")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    /*查询点赞数量,游客模式下也可查询*/
    @GetMapping("/video-likes")
    public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }//若令牌解析异常,说明为游客模式，那就不解析令牌了,走空执行
        Map<String, Object> result = videoService.getVideoLikes(videoId, userId);
        return new JsonResponse<>(result);
    }

    /*视频收藏*/
    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection) {
        Long userId = userSupport.getCurrentUserId();
        videoCollection.setUserId(userId);
        videoService.addVideoCollection(videoCollection);
        return JsonResponse.success();
    }

    /*取消视频收藏*/
    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId, userId);
        return JsonResponse.success();
    }


    /*显示视频收藏总数，游客模式和用户模式区分对待*/
    @GetMapping("/video-collections")
    public JsonResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }//游客模式下空执行
        Map<String, Object> map = videoService.getVideoCollections(videoId, userId);
        return new JsonResponse<>(map);
    }

    /*用户新建收藏分组*/
    @PostMapping("/collection-group")
    public JsonResponse<String> addCollectionGroup(@RequestBody CollectionGroup collectionGroup) {
        Long userId = userSupport.getCurrentUserId();
        collectionGroup.setUserId(userId);
        videoService.addCollectionGroup(collectionGroup);
        return JsonResponse.success();
    }

    /*用户删除已有的收藏分组，默认这个分组是存在的，先不做刁钻的判断了*/
    @DeleteMapping("/collection-group")
    public JsonResponse<String> deleteCollectionGroup(@RequestParam Long id) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteCollectinoGroup(id, userId);
        return JsonResponse.success();
    }

    /*用户修改已有的收藏分组的信息，同样先不做刁钻的判断,做简单实现先*/
    @PutMapping("/collection-group")
    public JsonResponse<String> updateCollectionGroup(@RequestBody CollectionGroup collectionGroup) {
        Long userId = userSupport.getCurrentUserId();
        collectionGroup.setUserId(userId);
        videoService.updateCollectionGroup(collectionGroup);
        return JsonResponse.success();
    }

    /*用户查询收藏分组信息,其内包含冗余字段，也就是每个分组内的收藏的视频信息*/
    @GetMapping("/collection-group")
    public JsonResponse<List<CollectionGroup>> getCollectionGroups() {
        Long userId = userSupport.getCurrentUserId();
        List<CollectionGroup> result = videoService.getCollectionGroups(userId);
        return new JsonResponse<>(result);
    }

    /*视频投币*/
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin) {
        //不catch异常了,因为只有登录了的用户才有资格投币
        Long userId = userSupport.getCurrentUserId();
        videoCoin.setUserId(userId);
        videoService.addVideoCoins(videoCoin);
        return JsonResponse.success();
    }

    /*查询视频硬币数量,游客和登录用户区别对待*/
    @GetMapping("/video-coins")
    public JsonResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }
        Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
        return new JsonResponse<>(result);
    }

    /*添加视频评论*/
    @PostMapping("/video-comments")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment) {
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoComment(videoComment, userId);
        return JsonResponse.success();
    }

    /*分页查询视频评论*/
    @GetMapping("/video-comments")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer no,
                                                                        @RequestParam Integer size,
                                                                        @RequestParam Long videoId) {

        PageResult<VideoComment> pageList = videoService.pageListVideoComments(no, size, videoId);
        return new JsonResponse<>(pageList);
    }

    /*用户点击视频进入视频界面后,需返回给前端视频详情,以便前端加载视频界面*/
    @GetMapping("/video-details")
    public JsonResponse<Map<String, Object>> getVideoDetails(@RequestParam Long videoId) {
        Map<String, Object> result = videoService.getVideoDetails(videoId);
        return new JsonResponse<>(result);
    }

    /*添加视频观看记录,规则是:对于同一条视频,登录用户每天只能产生一条观看记录,游客也是*/
    @PostMapping("/video-views")
    public JsonResponse<String> addVideoView(@RequestParam Long videoId,
                                             HttpServletRequest request) {
        VideoView videoView = new VideoView();
        videoView.setVideoId(videoId);
        Long userId;
        try {
            userId = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
        } catch (Exception e) {
        }//若为游客则空实现

        videoService.addVideoView(videoView, request);
        return JsonResponse.success();
    }

    /*查询视频播放量*/
    @GetMapping("/video-view-counts")
    public JsonResponse<Integer>getVideoViewCounts(@RequestParam Long videoId){
        Integer count=videoService.getVideoViewCounts(videoId);
        return new JsonResponse<>(count);
    }

    /*基于兴趣相同的用户获取视频内容推荐*/
    @GetMapping("/recommendations")
    public JsonResponse<List<Video>>recommend() throws TasteException {
        Long userId = userSupport.getCurrentUserId();
        //业务层中将调用现成的推荐引擎依赖算法,输入用户信息,自动返回推荐视频的列表
        List<Video>list=videoService.recommend(userId);
        return new JsonResponse<>(list);
    }



}
