import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <Container className="mt-4">
      <Row className="mb-5">
        <Col>
          <div className="text-center">
            <h1 className="display-4 mb-4">EV Data Analytics Marketplace</h1>
            <p className="lead mb-4">
              Chợ dữ liệu phân tích xe điện - Nơi kết nối dữ liệu và phân tích thông minh
            </p>
            <Button as={Link} to="/data-packages" variant="primary" size="lg" className="me-3">
              Khám phá dữ liệu
            </Button>
            <Button as={Link} to="/register" variant="outline-primary" size="lg">
              Tham gia ngay
            </Button>
          </div>
        </Col>
      </Row>

      <Row className="mb-5">
        <Col md={4}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>Người dùng dữ liệu</Card.Title>
              <Card.Text>
                Tìm kiếm, mua và sử dụng dữ liệu EV để phát triển ứng dụng, nghiên cứu và phân tích thị trường.
              </Card.Text>
              <ul className="list-unstyled text-start">
                <li>✓ Tìm kiếm dữ liệu theo nhu cầu</li>
                <li>✓ Mua gói dữ liệu phù hợp</li>
                <li>✓ Công cụ phân tích tích hợp</li>
                <li>✓ API truy cập dữ liệu</li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>Nhà cung cấp dữ liệu</Card.Title>
              <Card.Text>
                Chia sẻ dữ liệu EV của bạn và tạo ra nguồn thu nhập từ việc cung cấp dữ liệu chất lượng.
              </Card.Text>
              <ul className="list-unstyled text-start">
                <li>✓ Upload và quản lý dữ liệu</li>
                <li>✓ Thiết lập giá và chính sách</li>
                <li>✓ Theo dõi doanh thu</li>
                <li>✓ Bảo mật và ẩn danh hóa</li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="h-100 text-center">
            <Card.Body>
              <Card.Title>Quản trị viên</Card.Title>
              <Card.Text>
                Quản lý hệ thống, kiểm duyệt dữ liệu và đảm bảo chất lượng của marketplace.
              </Card.Text>
              <ul className="list-unstyled text-start">
                <li>✓ Quản lý người dùng</li>
                <li>✓ Kiểm duyệt dữ liệu</li>
                <li>✓ Xử lý thanh toán</li>
                <li>✓ Báo cáo và thống kê</li>
              </ul>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mb-5">
        <Col>
          <Card className="bg-light">
            <Card.Body className="text-center">
              <h3>Loại dữ liệu có sẵn</h3>
              <Row className="mt-3">
                <Col md={3}>
                  <h5>Dữ liệu Pin</h5>
                  <p>SoC, SoH, hiệu suất sạc</p>
                </Col>
                <Col md={3}>
                  <h5>Hành vi lái xe</h5>
                  <p>Quãng đường, tốc độ, mô hình sử dụng</p>
                </Col>
                <Col md={3}>
                  <h5>Dữ liệu sạc</h5>
                  <p>Tần suất sạc, thời gian sạc, vị trí</p>
                </Col>
                <Col md={3}>
                  <h5>Giao dịch V2G</h5>
                  <p>Bán điện ngược lưới, giá điện</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Home;
