import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Table, Badge } from 'react-bootstrap';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';
import DataPackageModal from '../components/DataPackageModal';

const ProviderDashboard = () => {
  const { user } = useAuth();
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [dataSources, setDataSources] = useState([]);
  const [selectedDataSourceId, setSelectedDataSourceId] = useState(null);

  useEffect(() => {
    if (user && user.role === 'DATA_PROVIDER') {
      fetchMyPackages();
      fetchDataSources();
    }
  }, [user]);

  const fetchMyPackages = async () => {
    try {
      const response = await axios.get(`/api/data-packages/by-provider/${user.id}`);
      setPackages(response.data);
    } catch (err) {
      setError('Không thể tải danh sách gói dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  const fetchDataSources = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`/api/data-sources/by-provider/${user.id}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      setDataSources(response.data);
      if (response.data.length > 0) {
        setSelectedDataSourceId(response.data[0].id);
      }
    } catch (err) {
      console.error('Không thể tải danh sách nguồn dữ liệu', err);
    }
  };

  const handleCreatePackage = () => {
    if (!selectedDataSourceId) {
      setError('Vui lòng tạo nguồn dữ liệu trước khi tạo gói dữ liệu');
      return;
    }
    setSelectedPackage(null);
    setShowModal(true);
  };

  const handleEditPackage = (pkg) => {
    setSelectedPackage(pkg);
    setShowModal(true);
  };

  const handleModalClose = () => {
    setShowModal(false);
    setSelectedPackage(null);
  };

  const handleModalSuccess = () => {
    fetchMyPackages();
    setError('');
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      'ACTIVE': { variant: 'success', text: 'Hoạt động' },
      'INACTIVE': { variant: 'danger', text: 'Không hoạt động' },
      'PENDING_APPROVAL': { variant: 'warning', text: 'Chờ duyệt' }
    };
    
    const statusInfo = statusMap[status] || { variant: 'secondary', text: status };
    return <Badge bg={statusInfo.variant}>{statusInfo.text}</Badge>;
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  };

  if (!user || user.role !== 'DATA_PROVIDER') {
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
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h2>Dashboard Nhà cung cấp dữ liệu</h2>
              <p className="text-muted">Quản lý gói dữ liệu của bạn</p>
            </div>
            <Button variant="primary" onClick={handleCreatePackage}>
              + Tạo gói dữ liệu mới
            </Button>
          </div>
        </Col>
      </Row>

      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-primary">{packages.length}</h3>
              <p className="text-muted mb-0">Tổng gói dữ liệu</p>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-success">
                {packages.filter(p => p.status === 'ACTIVE').length}
              </h3>
              <p className="text-muted mb-0">Đang hoạt động</p>
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
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <h3 className="text-info">0</h3>
              <p className="text-muted mb-0">Doanh thu (VND)</p>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Danh sách gói dữ liệu</h5>
            </Card.Header>
            <Card.Body>
              {packages.length === 0 ? (
                <div className="text-center py-4">
                  <p className="text-muted">Bạn chưa có gói dữ liệu nào.</p>
                  <Button variant="primary" onClick={handleCreatePackage}>
                    Tạo gói dữ liệu đầu tiên
                  </Button>
                </div>
              ) : (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>Tên gói</th>
                      <th>Loại dữ liệu</th>
                      <th>Định dạng</th>
                      <th>Giá</th>
                      <th>Trạng thái</th>
                      <th>Hành động</th>
                    </tr>
                  </thead>
                  <tbody>
                    {packages.map((pkg) => (
                      <tr key={pkg.id}>
                        <td>
                          <div>
                            <strong>{pkg.name}</strong>
                            <br />
                            <small className="text-muted">{pkg.description}</small>
                          </div>
                        </td>
                        <td>{pkg.dataType?.replace('_', ' ')}</td>
                        <td>{pkg.format}</td>
                        <td>{formatPrice(pkg.price)}</td>
                        <td>{getStatusBadge(pkg.status)}</td>
                        <td>
                          <Button 
                            variant="outline-primary" 
                            size="sm"
                            onClick={() => handleEditPackage(pkg)}
                            className="me-2"
                          >
                            Chỉnh sửa
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mt-4">
        <Col>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Hướng dẫn sử dụng</h5>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col md={4}>
                  <h6>1. Tạo gói dữ liệu</h6>
                  <p className="text-muted">
                    Upload file dữ liệu và tạo gói dữ liệu với thông tin chi tiết.
                  </p>
                </Col>
                <Col md={4}>
                  <h6>2. Thiết lập giá</h6>
                  <p className="text-muted">
                    Đặt giá cho gói dữ liệu theo mô hình phù hợp (một lần, thuê bao, theo lượt tải).
                  </p>
                </Col>
                <Col md={4}>
                  <h6>3. Quản lý bán hàng</h6>
                  <p className="text-muted">
                    Theo dõi doanh thu, lượt tải và phản hồi từ người dùng.
                  </p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <DataPackageModal
        show={showModal}
        handleClose={handleModalClose}
        packageData={selectedPackage}
        dataSourceId={selectedDataSourceId}
        onSuccess={handleModalSuccess}
      />
    </Container>
  );
};

export default ProviderDashboard;
