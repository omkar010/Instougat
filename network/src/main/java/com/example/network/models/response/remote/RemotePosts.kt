package com.example.network.models.response.remote

import com.example.network.models.response.domain.Post
import kotlinx.serialization.Serializable

@Serializable
data class RemotePosts(
    val data: Data
) {
    @Serializable
    data class Data(
        val user: User
    ) {
        @Serializable
        data class User(
            val edge_owner_to_timeline_media: EdgeOwnerToTimelineMedia
        ) {
            @Serializable
            data class EdgeOwnerToTimelineMedia(
                val page_info: PageInfo,
                val edges: List<Edge>
            ) {
                @Serializable
                data class PageInfo(
                    val has_next_page: Boolean,
                    val end_cursor: String
                )

                @Serializable
                data class Edge(
                    val node: Node
                ) {
                    @Serializable
                    data class Node(
                        val __typename: String,
                        val display_url: String,
                        val display_resources: List<ImageRes>,
                        val video_url: String?,
                        val edge_sidecar_to_children: EdgeSideCarToChildren? = null,
                        val edge_media_preview_like : Like
                    ) {
                        @Serializable
                        data class Like(
                            val count: Long
                        )

                        @Serializable
                        data class ImageRes(
                            val src: String,
                            val config_width: Int,
                            val config_height: Int
                        )

                        @Serializable
                        data class EdgeSideCarToChildren(
                            val edges: List<Edge>
                        ) {
                            @Serializable
                            data class Edge(
                                val node: Node
                            ) {
                                @Serializable
                                data class Node(
                                    val __typename: String,
                                    val display_url: String,
                                    val is_video: Boolean,
                                    val video_url: String?
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class PostsPage(
    val posts: List<Post>,
    val hasNextPage: Boolean,
    val endCursor: String
)

fun RemotePosts.toDomainPosts(): PostsPage {

    return PostsPage(
        posts = this.data.user.edge_owner_to_timeline_media.edges.map { edge ->
            edge.node.toDomainPost()
        },
        hasNextPage = this.data.user.edge_owner_to_timeline_media.page_info.has_next_page,
        endCursor = this.data.user.edge_owner_to_timeline_media.page_info.end_cursor
    )

}

fun RemotePosts.Data.User.EdgeOwnerToTimelineMedia.Edge.Node.toDomainPost(): Post {
    val urlType = when (this.__typename) {
        "GraphVideo" -> Post.UrlType.VIDEO
        "GraphImage" -> Post.UrlType.IMAGE
        "GraphSidecar" -> Post.UrlType.SIDECAR
        else -> Post.UrlType.UNKNOWN
    }
    val displayResource = this.display_resources.first()

    return Post(
        urlType = urlType,
        displayUrl = displayResource.src,
        url = when (urlType) {
            Post.UrlType.VIDEO -> this.video_url ?: displayResource.src
            Post.UrlType.IMAGE -> this.display_url
            Post.UrlType.SIDECAR -> displayResource.src // Placeholder, children will have individual URLs
            Post.UrlType.UNKNOWN -> displayResource.src
        },
        aspectRatio = displayResource.config_width.toFloat() /displayResource.config_height.toFloat(),
        children = this.edge_sidecar_to_children?.edges?.map {
            Post.Children(
                urlType = if (it.node.is_video) Post.UrlType.VIDEO else Post.UrlType.IMAGE,
                url = if (it.node.is_video) it.node.video_url!! else it.node.display_url
            )
        },
        likes = this.edge_media_preview_like.count
    )
}
