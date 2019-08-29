// 「全て選択」チェックで全てにチェック付く
// name 属性がすべて同じ場合（ここでは className がname属性で統一）
// すべて選択のボックスのname属性を all にする
function AllChecked(){
    var all = document.form.all.checked;
    for (var i=0; i<document.form.className.length; i++){
        document.form.className[i].checked = all;
    }
}

// 一つでもチェックを外すと「全て選択」のチェック外れる
function DisChecked(){
    var checks = document.form.className;
    var checksCount = 0;
    for (var i=0; i<checks.length; i++){
        if(checks[i].checked == false){
            document.form.all.checked = false;
        }else{
            checksCount += 1;
            if(checksCount == checks.length){
                document.form.all.checked = true;
            }
        }
    }
}
