define('LoanManagementRouterPath', [], function() {
	return {
		routerPath: {
            LoanManagement_paymentAgreement:"../../LoanManagement/scripts/paymentAgreement/paymentAgreement"
        },
		services: {
            PsnLoanPaymentSignContractQuery: {
                depends: [],
                method: 'PsnLoanPaymentSignContractQuery',
                params: {},
                url: 'BII',
                dataRestruct: function(data){
                    return data;
                }
            }
		}
    }
});