function writeComment() // no ';' here
{
    if (writeCommentButtonStatus.firstChild.data === "댓글 작성") {
        writeCommentButtonStatus.firstChild.data = "작성 창 닫기";
        writeCommentPageStatus.style.display = "block";
    }
    else {
        writeCommentButtonStatus.firstChild.data = "댓글 작성";
        writeCommentPageStatus.style.display = "none";
    }
}