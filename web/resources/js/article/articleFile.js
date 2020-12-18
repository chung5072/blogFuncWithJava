function getFile() // no ';' here
{
    if (getFileButtonStatus.firstChild.data === "파일 다운로드") {
        getFileButtonStatus.firstChild.data = "다운로드 닫기";
        getFilePageStatus.style.display = "block";
    }
    else {
        getFileButtonStatus.firstChild.data = "파일 다운로드";
        getFilePageStatus.style.display = "none";
    }
}
