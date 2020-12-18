function openWin(url) {
    ReportPrintPreview = window.open("about:blank", "ReportPrintPreview", "width=666,height=700left=250,top=50,dependent=yes,menubar=no,status=no,resizable=yes,toolbar=no,scrollbars=yes");
    ReportPrintPreview.location.href = url;
    ReportPrintPreview.focus();
    return false;
}