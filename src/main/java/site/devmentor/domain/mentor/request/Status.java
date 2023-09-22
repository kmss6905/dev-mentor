package site.devmentor.domain.mentor.request;

public enum Status {
  WAITING {
    @Override
    public boolean isNotChangeableTo(Status newStatus) {
      return false; // WAITING 상태는 다른 어떤 상태로의 전환도 허용함
    }

    @Override
    public String errorMessage() {
      return null; // 없음
    }
  },
  PENDING {
    @Override
    public boolean isNotChangeableTo(Status newStatus) {
      return newStatus == WAITING;
    }

    @Override
    public String errorMessage() {
      return this.name()+" 상태의 경우 다시 " + WAITING.name() + "으로 돌아갈 수 없습니다.";
    }
  },
  ACCEPTED {
    @Override
    public boolean isNotChangeableTo(Status newStatus) {
      return newStatus == WAITING || newStatus == PENDING || newStatus == DENIED;
    }
    @Override
    public String errorMessage() {
      return this.name()+" 상태의 경우 어떤 상태로 변경할 수 없습니다.";
    }
  },
  DENIED {
    @Override
    public boolean isNotChangeableTo(Status newStatus) {
      return true; // DENIED 상태는 다른 상태로의 전환을 허용하지 않음
    }

    @Override
    public String errorMessage() {
      return this.name()+"상태는 다른 상태로의 전환을 허용하지 않음";
    }
  };

  public abstract boolean isNotChangeableTo(Status newStatus);

  public abstract String errorMessage();

}
