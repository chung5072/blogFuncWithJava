function showComments() // no ';' here
{
    if (commentButtonStatus.firstChild.data === "댓글 보기") {
        commentButtonStatus.firstChild.data = "댓글 숨기기";
        commentListStatus.style.display = "block";
    }
    else {
        commentButtonStatus.firstChild.data = "댓글 보기";
        commentListStatus.style.display = "none";
    }
}