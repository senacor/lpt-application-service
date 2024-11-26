import http from 'k6/http';
import { Rate } from 'k6/metrics';
import { sleep } from 'k6';

const creditApplicationCreationRate = new Rate('failed_credit_application_creation');
const creditApplicationAcceptanceRate = new Rate('failed_credit_application_acceptance');

export const options = {
    vus: 10,
    duration: '60s',
    thresholds: {
        'failed_credit_application_creation': ['rate<0.1'],
        'failed_credit_application_acceptance': ['rate<0.1'],
        'http_req_duration': ['p(95)<400'],
    },
};

export default function () {
    const creditApplicationCreation = http.post('http://creditapplication-k8s-service.lpt.svc.cluster.local:8080/api/credit-applications', JSON.stringify({"creditAmount":10000.00,"firstName":"Jim","lastName":"Beam","zipCode":"12345","occupation":"IT","monthlyNetIncome":5000.00,"monthlyExpenses":2500.00}), {
        headers: { 'Content-Type': 'application/json' },
    });
    console.log(creditApplicationCreation.status)
    console.log(creditApplicationCreation.body)
    creditApplicationCreationRate.add(creditApplicationCreation.status !== 200);
    const applicationId = creditApplicationCreation.body.id;
    // after receiving the credit decision, the customer decides within 5 seconds to accept or decline the credit application
    sleep(5);
    const creditApplicationAcceptance = http.post(`http://creditapplication-k8s-service.lpt.svc.cluster.local:8080/api/credit-applications/${applicationId}`, JSON.stringify({}), {
        headers: { 'Content-Type': 'application/json' },
    });
    creditApplicationAcceptanceRate.add(creditApplicationAcceptance.status !== 202);
}