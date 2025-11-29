import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Alert, Badge } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../utils/axiosConfig';

const DataPackageDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [packageData, setPackageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchPackageDetail();
  }, [id]);

  const fetchPackageDetail = async () => {
    try {
      const response = await api.get(`/api/data-packages/${id}`);
      setPackageData(response.data);
    } catch (err) {
      setError('Không thể tải thông tin gói dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  const handlePurchase = () => {
    // TODO: Implement purchase logic
    alert('Chức năng mua dữ liệu sẽ được triển khai trong phiên bản tiếp theo');
  };

  const handleDownload = () => {
    // TODO: Implement download logic
    alert('Chức năng tải xuống sẽ được triển khai trong phiên bản tiếp theo');
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

  const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

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

  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
        <Button onClick={() => navigate('/data-packages')}>
          Quay lại danh sách
        </Button>
      </Container>
    );
  }

  if (!packageData) {
    return (
      <Container className="mt-4">
        <Alert variant="info">Không tìm thấy gói dữ liệu</Alert>
        <Button onClick={() => navigate('/data-packages')}>
          Quay lại danh sách
        </Button>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <Button 
            variant="outline-secondary" 
            onClick={() => navigate('/data-packages')}
            className="mb-3"
          >
            ← Quay lại danh sách
          </Button>
        </Col>
      </Row>

      <Row>
        <Col md={8}>
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <h3 className="mb-0">{packageData.name}</h3>
              {getStatusBadge(packageData.status)}
            </Card.Header>
            <Card.Body>
              <p className="lead">{packageData.description}</p>
              
              <Row className="mb-4">
                <Col md={6}>
                  <h5>Thông tin chi tiết</h5>
                  <ul className="list-unstyled">
                    <li><strong>Loại dữ liệu:</strong> {packageData.dataType?.replace('_', ' ')}</li>
                    <li><strong>Định dạng:</strong> {packageData.format}</li>
                    <li><strong>Kích thước:</strong> {formatFileSize(packageData.size || 0)}</li>
                    <li><strong>Mô hình giá:</strong> {packageData.pricingModel?.replace('_', ' ')}</li>
                  </ul>
                </Col>
                <Col md={6}>
                  <h5>Nguồn dữ liệu</h5>
                  <ul className="list-unstyled">
                    <li><strong>Tên nguồn:</strong> {packageData.dataSource?.name}</li>
                    <li><strong>Loại nguồn:</strong> {packageData.dataSource?.type?.replace('_', ' ')}</li>
                    <li><strong>Nhà cung cấp:</strong> {packageData.dataSource?.dataProvider?.companyName}</li>
                  </ul>
                </Col>
              </Row>

              <div className="bg-light p-3 rounded">
                <h6>Mô tả chi tiết</h6>
                <p className="mb-0">
                  Gói dữ liệu này cung cấp thông tin chi tiết về {packageData.dataType?.toLowerCase()} 
                  trong định dạng {packageData.format}. Dữ liệu được thu thập từ {packageData.dataSource?.type?.toLowerCase()} 
                  và đã được xử lý để đảm bảo chất lượng và tính nhất quán.
                </p>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col md={4}>
          <Card className="sticky-top">
            <Card.Body>
              <div className="text-center mb-4">
                <h2 className="price-tag">{formatPrice(packageData.price)}</h2>
                <p className="text-muted">Mô hình: {packageData.pricingModel?.replace('_', ' ')}</p>
              </div>

              <div className="d-grid gap-2">
                <Button 
                  variant="primary" 
                  size="lg"
                  onClick={handlePurchase}
                  disabled={packageData.status !== 'ACTIVE'}
                >
                  Mua ngay
                </Button>
                
                <Button 
                  variant="outline-primary"
                  onClick={handleDownload}
                  disabled={packageData.status !== 'ACTIVE'}
                >
                  Tải xuống
                </Button>
              </div>

              {packageData.status !== 'ACTIVE' && (
                <Alert variant="warning" className="mt-3">
                  Gói dữ liệu này hiện không khả dụng để mua.
                </Alert>
              )}

              <div className="mt-4">
                <h6>Thông tin bổ sung</h6>
                <ul className="list-unstyled small">
                  <li>✓ Dữ liệu được cập nhật thường xuyên</li>
                  <li>✓ Hỗ trợ API truy cập</li>
                  <li>✓ Bảo mật và ẩn danh hóa</li>
                  <li>✓ Hỗ trợ kỹ thuật 24/7</li>
                </ul>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DataPackageDetail;
