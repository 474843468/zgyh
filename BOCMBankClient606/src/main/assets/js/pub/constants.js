//定义储蓄类菜单
var deposit_menu_names = ["整存整取","零存整取","整存零取","存本取息","活期储蓄","定活两便"];
var deposit_menu_values = ["zczq","lczq","zclq","cbqx","hqcx","dhlb"];
//定义贷款类菜单
var loan_menu_names = ["等额还款","个人房贷","提前还款"];
var loan_menu_values = ["dehk","grfd","tqhk"];
//定义基金类菜单
var invest_menu_names = ["认购计算","申购计算","赎回计算","基金转换"];
var invest_menu_values = ["rg","sg","sh","jjzh"];
//定义其他类菜单
var other_menu_names = [];
var other_menu_values = [];

var foreign_menu_names = ["外币兑换"];
var foreign_menu_values = ["wbdh"];

var menu_names = ["储蓄计算器","贷款计算器","基金计算器","外汇计算器"];
var menu_values = [[deposit_menu_names,deposit_menu_values],
                   [loan_menu_names,loan_menu_values],
                   [invest_menu_names,invest_menu_values],
                   [foreign_menu_names,foreign_menu_values]];

var foreign_currency_names = ["人民币","港币","美元","英镑","加拿大元","澳元","日元"];
var foreign_currency_values = ["CNY","HKD","USD","GBP","CAD","AUD","JPY"];
var foreign_currency_rates = [1,0.81,6.26,9.56,6.16,6.53,0.06];

var hqcx_rate = "0.35";

var zczq_saveTerm_names = ["三个月","六个月","一年","二年","三年","五年"];
var zczq_saveTerm_values = [0.25,0.5,1,2,3,5];
var zczq_save_rates = ["2.85","3.05","3.25","3.75","4.25","4.75"];
var zclq_save_rates = ["2.85","2.90","3.00"];

var dk_loanTerm_values = [0.5,1,3,5,6];
var dk_loan_rates = [5.60,6.00,6.15,6.40,6.55];

var dk_house_loanTerm_values = [5,6];
var dk_house_loanTerm_rates = [4.00,4.50];

var pub_explain = ["使用须知","1.计算器的数据与计算结果仅供参考。具体以办理业务和交易实际结果为准。","2.计算器在计算利息的过程中，一年以365天为标准，请用户知悉。"];


