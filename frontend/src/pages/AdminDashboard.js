import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Table, Badge } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthContext';
import api from '../utils/axiosConfig';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user && user.role === 'ADMIN') {
      fetchDashboardData();
    }
  }, [user]);

  const fetchDashboardData = async () => {
    try {
      // Fetch users and packages data
      const [usersResponse, packagesResponse] = await Promise.all([
        api.get('/api/users'),
        api.get('/api/data-packages')
      ]);
      
      setUsers(usersResponse.data);
      setPackages(packagesResponse.data);
    } catch (err) {
      setError('Không thể tải dữ liệu dashboard');
    } finally {
      setLoading(false);
    }
  };

  const handleApprovePackage = async (packageId) => {
    try {
      const packageToUpdate = packages.find(p => p.id === packageId);
      
      if (!packageToUpdate) return;

      const updatedPackage = {
        ...packageToUpdate,
        status: 'ACTIVE'
      };

      await api.put(`/api/data-packages/${packageId}`, updatedPackage);

      // Refresh dashboard data
      fetchDashboardData();
      setError('');
    } catch (err) {
      setError('Không thể duyệt gói dữ liệu: ' + (err.response?.data?.error || err.message));
    }
  };

  const handleRejectPackage = async (packageId) => {
    try {
      const packageToUpdate = packages.find(p => p.id === packageId);
      
      if (!packageToUpdate) return;

      const updatedPackage = {
        ...packageToUpdate,
        status: 'INACTIVE'
      };

      await api.put(`/api/data-packages/${packageId}`, updatedPackage);

      // Refresh dashboard data
      fetchDashboardData();
      setError('');
    } catch (err) {
      setError('Không thể từ chối gói dữ liệu: ' + (err.response?.data?.error || err.message));
    }
  };

  const handleDeletePackage = async (packageId) => {
    try {
      await api.delete(`/api/data-packages/${packageId}`);

      // Refresh dashboard data
      fetchDashboardData();
      setError('');
    } catch (err) {
      setError('Khong the xoa goi du lieu: ' + (err.response?.data?.error || err.message));
    }
  };

  const handleSuspendUser = async (userId) => {
    try {
      await api.put(`/api/users/${userId}/status`, { status: 'SUSPENDED' });

      // Refresh dashboard data
      fetchDashboardData();
      setError('');
    } catch (err) {
      setError('Không thể tạm khóa người dùng: ' + (err.response?.data?.error || err.message));
    }
  };

  const handleActivateUser = async (userId) => {
    try {
      await api.put(`/api/users/${userId}/status`, { status: 'ACTIVE' });

      // Refresh dashboard data
      fetchDashboardData();
      setError('');
    } catch (err) {
      setError('Không thể kích hoạt người dùng: ' + (err.response?.data?.error || err.message));
    }
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      'ACTIVE': { variant: 'success', text: 'Hoạt động' },
      'INACTIVE': { variant: 'danger', text: 'Không hoạt động' },
      'SUSPENDED': { variant: 'warning', text: 'Tạm khóa' },
      'PENDING_APPROVAL': { variant: 'warning', text: 'Chờ duyệt' }
    };
    
    const statusInfo = statusMap[status] || { variant: 'secondary', text: status };
    return <Badge bg={statusInfo.variant}>{statusInfo.text}</Badge>;
  };

  const getRoleBadge = (role) => {
    const roleMap = {
      'DATA_CONSUMER': { variant: 'info', text: 'Người dùng' },
      'DATA_PROVIDER': { variant: 'primary', text: 'Nhà cung cấp' },
      'ADMIN': { variant: 'danger', text: 'Admin' }
    };
    
    const roleInfo = roleMap[role] || { variant: 'secondary', text: role };
    return <Badge bg={roleInfo.variant}>{roleInfo.text}</Badge>;
  };

  if (!user || user.role !== 'ADMIN') {
    return (
      <Container className="mt-4">
        <Alert variant="danger">
          Bạn không có quyền truy cập trang này.
        </Alert>
      </Container>
    );
  }

  if (loading) {
    return (
      <Container className="mt-4">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Đang tải...</span>
          </div>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row className="mb-4">
        <Col>
          <h2>Dashboard Quản trị viên</h2>
          <p className="text-muted">Quản lý hệ thống và người dùng</p>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-primary">{users.length}</h3>
              <p className="text-muted mb-0">Tổng người dùng</p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-success">
                {users.filter(u => u.role === 'DATA_CONSUMER').length}
              </h3>
              <p className="text-muted mb-0">Người dùng dữ liệu</p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-info">
                {users.filter(u => u.role === 'DATA_PROVIDER').length}
              </h3>
              <p className="text-muted mb-0">Nhà cung cấp</p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-warning">
                {packages.filter(p => p.status === 'PENDING_APPROVAL').length}
              </h3>
              <p className="text-muted mb-0">Chờ duyệt</p>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Gói dữ liệu chờ duyệt</h5>
            </Card.Header>
            <Card.Body>
              {packages.filter(p => p.status === 'PENDING_APPROVAL').length === 0 ? (
                <p className="text-muted text-center py-3">Không có gói dữ liệu nào chờ duyệt.</p>
              ) : (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>Tên gói</th>
                      <th>Nhà cung cấp</th>
                      <th>Loại dữ liệu</th>
                      <th>Giá</th>
                      <th>Hành động</th>
                    </tr>
                  </thead>
                  <tbody>
                    {packages.filter(p => p.status === 'PENDING_APPROVAL').map((pkg) => (
                      <tr key={pkg.id}>
                        <td>
                          <div>
                            <strong>{pkg.name}</strong>
                            <br />
                            <small className="text-muted">{pkg.description}</small>
                          </div>
                        </td>
                        <td>{pkg.dataSource?.dataProvider?.companyName}</td>
                        <td>{pkg.dataType?.replace('_', ' ')}</td>
                        <td>{new Intl.NumberFormat('vi-VN', {
                          style: 'currency',
                          currency: 'VND'
                        }).format(pkg.price)}</td>
                        <td>
                          <Button 
                            variant="success" 
                            size="sm"
                            onClick={() => handleApprovePackage(pkg.id)}
                            className="me-2"
                          >
                            Duyệt
                          </Button>
                          <Button 
                            variant="danger" 
                            size="sm"
                            onClick={() => handleRejectPackage(pkg.id)}
                          >
                            Từ chối
                          </Button>
                        
                          <Button 
                            variant="outline-danger" 
                            size="sm"
                            className="ms-2"
                            onClick={() => handleDeletePackage(pkg.id)}
                          >
                            Xóa
                          </Button></td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Danh sách người dùng</h5>
            </Card.Header>
            <Card.Body>
              <Table responsive>
                <thead>
                  <tr>
                    <th>Tên đăng nhập</th>
                    <th>Email</th>
                    <th>Vai trò</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                    <th>Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td>{user.username}</td>
                      <td>{user.email}</td>
                      <td>{getRoleBadge(user.role)}</td>
                      <td>{getStatusBadge(user.status)}</td>
                      <td>{new Date(user.createdDate).toLocaleDateString('vi-VN')}</td>
                      <td>
                        {user.status === 'ACTIVE' && user.role !== 'ADMIN' && (
                          <Button 
                            variant="warning" 
                            size="sm"
                            onClick={() => handleSuspendUser(user.id)}
                            className="me-2"
                          >
                            Tạm khóa
                          </Button>
                        )}
                        {user.status === 'SUSPENDED' && (
                          <Button 
                            variant="success" 
                            size="sm"
                            onClick={() => handleActivateUser(user.id)}
                          >
                            Kích hoạt
                          </Button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AdminDashboard;
