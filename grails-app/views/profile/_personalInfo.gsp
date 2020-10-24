<%@ page import="com.grailslab.CommonUtils" %>
<div class="row">
    <div class="col-md-12">
        <div class="col-md-4">
            <h3>Employee Information</h3>

            <div class="well">
                <address>
                    <strong>EmpID -</strong>
                    ${employee?.empID}
                </address>
                <address>
                    <strong>Designation -</strong>
                    ${designation}
                </address>
                <address>
                    <strong>Joining Date -</strong>
                    ${CommonUtils.getUiDateStr(employee?.joiningDate)}
                </address>
                <address>
                    <strong>Conformation Date -</strong>
                    ${CommonUtils.getUiDateStr(employee?.confirmationDate)}
                </address>
                <address>
                    <strong>Card Number -</strong>
                    ${employee?.cardNo}
                </address>
                <address>
                    <strong>Employee Type -</strong>
                    ${category}
                </address>
                <address>
                    <strong>Salary Accounts -</strong>
                    ${employee?.salaryAccounts}
                </address>
                <address>
                    <strong>DPS Accounts -</strong>
                    ${employee?.dpsAccounts}
                </address>
            </div>
        </div>

        <div class="col-md-4">
            <h3>Personal Info</h3>

            <div class="well">
                <address>
                    <strong>Father's Name -</strong>
                    ${employee?.fathersName}
                </address><address>
                <strong>Mother's Name -</strong>
                ${employee?.mothersName}
            </address><address>
                <strong>Birth Date -</strong>
                ${CommonUtils.getUiDateStr(employee?.birthDate)}
            </address><address>
                <strong>Email -</strong>
                ${employee?.emailId}
            </address><address>
                <strong>Blood Group -</strong>
                ${employee?.bloodGroup}
            </address><address>
                <strong>Mobile -</strong>
                ${employee?.mobile}
            </address><address>
                <strong>facebook Link -</strong>
                ${employee?.fbId}
            </address>
            </div>
        </div>

        <div class="col-md-4">
            <h3>Address</h3>

            <div class="well">
                <address>
                    <strong>Present Address -</strong></br>
                    ${employee?.presentAddress}
                </address></br>
                <address>
                    <strong>Permanent Address -</strong></br>
                    ${employee?.permanentAddress}
                </address>
            </div>
        </div>
    </div>

    <div class="col-md-12">
        <h3 class="insert-personalInfo"><button class="btn btn-primary">Edit Details</button></h3>
    </div>
</div>