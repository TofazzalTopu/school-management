<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Configuration</title>
    <meta name="layout" content="moduleLibraryLayout"/>
    <asset:javascript src="angularJs/pagination.js"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Configuration" />
<div class="row" id="create-form-holder" ng-app="libraryConfiguration">
    <div ng-controller="configuration as configuration">
        <div class="col-sm-12" ng-form>
            <section class="panel">
                <header class="panel-heading">
                    Library Configuration
                </header>
                <div class="panel-body">
                    <form class="form-horizontal" role="form" id="createForm" name="createForm">
                        <div class="col-md-8 col-md-offset-2" >
                            <input ng-model="configuration.id" type="text" class="hidden form-control form-group">
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">User Type *</label>
                                </div>
                                <div class="col-md-7">
                                    <select class="selectpicker form-control" name="users" ng-model="configuration.memberType" required>
                                        <option ng-repeat="option in configuration.userOption" value="{{option}}">{{option}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">Max Day *</label>
                                </div>
                                <div class="col-md-7">
                                    <input type="text" ng-model="configuration.allowedDays" name="maximumDay" class="form-control addNumeric" placeholder="Maximum Day" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">Max Book *</label>
                                </div>
                                <div class="col-md-7">
                                    <input type="text" ng-model="configuration.numberOfBook" class="form-control addNumeric" placeholder="Maximum Book" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">Fine Amount</label>
                                </div>
                                <div class="col-md-7">
                                    <input type="text" ng-model="configuration.fineAmount" class="form-control addNumeric" placeholder="Fine Amount">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">Member Fee</label>
                                </div>
                                <div class="col-md-7">
                                    <input type="text" ng-model="configuration.memberFee" class="form-control addNumeric" placeholder="Member Fee">
                                </div>
                            </div>
                            <div class="form-group text-center">
                                <button ng-click="configuration.cancelBtn()" class="btn btn-default">Cancel</button>
                                <button ng-click="configuration.saveConfiguration()" class="btn btn-primary">{{configuration.addBtn}}</button>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="col-md-3">
                                <input type="text" ng-model="search" class="form-control form-group" placeholder="Search">
                            </div>
                        </div>

                        <div class="col-md-12">
                            <table class="table table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>User Type</th>
                                    <th>Max Day</th>
                                    <th>Max Book</th>
                                    <th>Fine Amount</th>
                                    <th>Member Fee</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <tbody dir-paginate="config in configuration.configList | itemsPerPage: totalPerPage| filter:search" total-items="totalItem" current-page="pagination.current">
                                <tr>
                                    <td>{{config.memberType}}</td>
                                    <td>{{config.allowedDays}}</td>
                                    <td>{{config.numberOfBook}}</td>
                                    <td>{{config.fineAmount}}</td>
                                    <td>{{config.memberFee}}</td>
                                    <td><i class="fa fa-edit " ng-click="configuration.editConfiguration(config)"></i>
                                        <i class="fa fa-close" ng-click="configuration.removeConfiguration(config)"></i>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <dir-pagination-controls
                                    max-size="5"
                                    direction-links="true"
                                    boundary-links="true"
                                    on-page-change="pageChanged(newPageNumber)">
                            </dir-pagination-controls>
                        </div>
                    </form>
                </div>
            </section>
        </div>
    </div>
</div>
<script>
    $('.addNumeric').numeric();
   var app = angular.module('libraryConfiguration', ['angularUtils.directives.dirPagination']);
   app.controller('configuration', function ($scope, $http, $filter) {

            var configuration = this;
            configuration.addBtn = 'Save';
            configuration.userOption = ['Teacher', 'Student', 'Guardian'];
            configuration.saveConfiguration = function () {
                if (configuration.memberType && configuration.allowedDays && configuration.numberOfBook){
                   var id;
                    if (!configuration.id){
                        id = 0;
                    } else {
                        id = configuration.id;
                    }
                    var saveConfig = {
                        id: id,
                        memberType: configuration.memberType,
                        allowedDays:  configuration.allowedDays,
                        numberOfBook: configuration.numberOfBook,
                        fineAmount: configuration.fineAmount,
                        memberFee: configuration.memberFee
                    };
                    $http.post('${createLink(controller: 'librarySetting', action: 'save')}', saveConfig) .then(function (response) {
                       if (!response.data.hasError){
                           if (id == 0){
                               configuration.configList.push(saveConfig);
                               configuration.memberType = '';
                               configuration.allowedDays = '';
                               configuration.numberOfBook = '';
                               configuration.fineAmount = '';
                               configuration.memberFee = '';
                               showSuccessMsg(response.data.message);
                           } else {
                               var updateConfig = $filter('filter')(configuration.configList, function (d) {return d.id === id;})[0];
                               var index = configuration.configList.indexOf(updateConfig);
                               configuration.configList[index] = saveConfig;
                               configuration.id = '';
                               configuration.memberType = '';
                               configuration.allowedDays = '';
                               configuration.numberOfBook = '';
                               configuration.fineAmount = '';
                               configuration.memberFee = '';
                               showInfoMsg(response.data.updateMsg);
                           }
                       } else {
                           showErrorMsg(response.data.errorMsg);
                       }
                        configuration.addBtn = 'Save';
                    });
                    }
                };

           configuration.configList = [];
           $scope.totalItem = 0;
           $scope.totalPerPage = 5;
           getResultsPage(1);

           $scope.pagination = {
               current: 1
           };
           $scope.pageChanged = function(newPage) {
               getResultsPage(newPage);
           };

           function getResultsPage(pageNumber) {
               $http.get('${createLink(controller: 'librarySetting', action: 'list')}?pageNumber=' + pageNumber + '&totalPerPage=' + $scope.totalPerPage).then(function(response) {
                   configuration.configList = response.data.items;
                       $scope.totalItem = response.data.total
                   });
           }

           $scope.sort = function (keyname) {
             $scope.sortKey = keyname;
             $scope.reverse = !$scope.reverse
             };

           configuration.editConfiguration = function (config) {
                configuration.addBtn = 'Update';
                configuration.id = config.id;
                configuration.memberType = config.memberType;
                configuration.allowedDays = config.allowedDays;
                configuration.numberOfBook = config.numberOfBook;
                configuration.fineAmount = config.fineAmount;
                configuration.memberFee = config.memberFee;
            };

            configuration.removeConfiguration = function (config) {
                var confirmDelete = confirm('Are you sure to delete?');
                if (confirmDelete == true){
                    var  deleteConfig = {
                        id:  config.id
                };
                $http.post('${createLink(controller: 'librarySetting', action: 'delete')}', deleteConfig) .then(function(response) {
                    var index = configuration.configList.indexOf(config);
                    configuration.configList.splice(index, 1);
                    configuration.memberType = '';
                    configuration.allowedDays = '';
                    configuration.numberOfBook = '';
                    configuration.fineAmount = '';
                    configuration.memberFee = '';
                showSuccessMsg(response.data.successMsg);
                });
                }
            };
            configuration.cancelBtn = function () {
            window.location.reload()
            };
        });
</script>
</body>
</html>
